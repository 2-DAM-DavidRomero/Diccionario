package com.example.diccionario

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diccionario.data.Palabra
import com.example.diccionario.data.RetrofitClient
import com.example.diccionario.ui.theme.DiccionarioTheme

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
    var palabra by remember { mutableStateOf<Palabra?>(null) }

    // Llamada a la API
    LaunchedEffect(name) {
        try {
            val response = RetrofitClient.api.getWord(name)
            palabra = response.data
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error: ${e.message}")
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Si la palabra aún no llegó
        if (palabra == null) {
            Text("Cargando…")
        } else {
            Text(text = "Palabra: ${palabra!!.word}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

            // Primer sense con sinónimos
            val firstSense = palabra!!
                .meanings
                .flatMap { it.senses }
                .firstOrNull { it.synonyms?.isNotEmpty() == true }

            val sinonimos = firstSense?.synonyms ?: emptyList()

            if (sinonimos.isNotEmpty()) {
                Text("Descripción: ${firstSense?.description}")
                sinonimos.forEach { sinonimo ->
                    Text("• $sinonimo")
                }
            } else {
                Text("Sin sinónimos 😭")
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