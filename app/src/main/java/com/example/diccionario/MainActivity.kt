package com.example.diccionario

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diccionario.data.Palabra
import com.example.diccionario.data.RetrofitClient
import com.example.diccionario.ui.theme.DiccionarioTheme
import kotlinx.coroutines.launch
import java.util.Locale

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
        modifier = Modifier.background(colorResource(R.color.primario))
            .padding(WindowInsets.statusBars.asPaddingValues()) // evita la barra superior / notch
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colorResource(R.color.secundario))
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
            OutlinedTextField(
                value = textPalabra,
                onValueChange = { textPalabra = it },
                placeholder = { Text("Palabra") },
                singleLine = true,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape) // botón redondo
                    .background(colorResource(R.color.primario)) // color del botón
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
                    .background(colorResource(R.color.primario))
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.15f)
                        .fillMaxWidth()
                        .background(colorResource(R.color.secundario)),
                        contentAlignment = Alignment.Center
                ){
                    if (palabra != null) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            text = palabra!!.word.uppercase(Locale.ROOT)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(colorResource(R.color.secundario))
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.secundario))
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (palabra != null) {
                            var cont = 1
                            Text(
                                fontSize = 15.sp,
                                color = colorResource(R.color.verde),
                                text = "Estimología:\n " +
                                        palabra!!.meanings[0].origin.raw
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 5.dp),
                                thickness = 1.dp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                text = "Definiciones:"
                            )
                            for (sense in palabra!!.meanings[0].senses) {
                                Text(
                                    fontSize = 15.sp,
                                    text = "$cont. ${sense.description}"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                cont ++
                            }
                            Text(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                text = "Sinónimos o afines de:"
                            )
                            for (sense in palabra!!.meanings[0].senses) {
                                if (sense.synonyms != null) {
                                    var lineaSinonimos = ""
                                    for (sinonimo in sense.synonyms) {
                                        lineaSinonimos += "$sinonimo, "
                                    }

                                    lineaSinonimos = lineaSinonimos.removeSuffix(", ")

                                    Text(
                                        text = "• $lineaSinonimos",
                                        fontSize = 14.sp,
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    cont++
                                }
                            }
                            Text(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                text = "Antónimos u opuestos de:"
                            )
                            for (sense in palabra!!.meanings[0].senses) {
                                if (sense.antonyms != null) {
                                    var lineaSinonimos = ""
                                    for (sinonimo in sense.antonyms) {
                                        lineaSinonimos += "$sinonimo, "
                                    }

                                    lineaSinonimos = lineaSinonimos.removeSuffix(", ")

                                    Text(
                                        text = "• $lineaSinonimos",
                                        fontSize = 14.sp,
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    cont++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiccionarioTheme {
        Greeting("Android")
    }
}
