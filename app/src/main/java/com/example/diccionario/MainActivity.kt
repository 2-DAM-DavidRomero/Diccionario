package com.example.diccionario

import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diccionario.data.Palabra
import com.example.diccionario.data.RetrofitClient
import com.example.diccionario.ui.theme.DiccionarioTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiccionarioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Comer",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var palabra by remember { mutableStateOf<Palabra?>(null) }
    var textPalabra by remember { mutableStateOf("") }




    Column(
        modifier = Modifier.background(colorResource(R.color.teal_700))
            .padding(WindowInsets.statusBars.asPaddingValues()) // evita la barra superior / notch
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colorResource(R.color.teal_200))
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = R.drawable.unnamed),
                contentDescription = "Logo Rae",
            )
            TextField(
                value = textPalabra,
                onValueChange = { textPalabra = it },
                placeholder = { Text("Palabra") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape) // botón redondo
                    .background(colorResource(R.color.purple_200)) // color del botón
                    .clickable {
                        scope.launch {
                            try {
                                val response = RetrofitClient.api.getWord(textPalabra)
                                palabra = response.data
                            } catch (e: Exception) {
                                Log.e("API_ERROR", "Error: ${e.message}")
                            }
                        }                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lupa),
                    contentDescription = "Buscar",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(11f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(R.color.teal_700))
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxWidth()
                        .background(colorResource(R.color.purple_200))
                ){
                    if (palabra != null) {
                        Text(text = palabra!!.word)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(colorResource(R.color.purple_200))
                )
            }
        }
    }




















//    Column(modifier = modifier.padding(16.dp)) {
//        // Si la palabra aún no llegó
//        if (palabra == null) {
//            Text("Cargando…")
//        } else {
//            Text(text = "Palabra: ${palabra!!.word}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
//
//            // Primer sense con sinónimos
//            val firstSense = palabra!!
//                .meanings
//                .flatMap { it.senses }
//                .firstOrNull { it.synonyms?.isNotEmpty() == true }
//
//            val sinonimos = firstSense?.synonyms ?: emptyList()
//
//            if (sinonimos.isNotEmpty()) {
//                Text("Descripción: ${firstSense?.description}")
//                sinonimos.forEach { sinonimo ->
//                    Text("• $sinonimo")
//                }
//            } else {
//                Text("Sin sinónimos 😭")
//            }
//        }
//    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiccionarioTheme {
        Greeting("Android")
    }
}
