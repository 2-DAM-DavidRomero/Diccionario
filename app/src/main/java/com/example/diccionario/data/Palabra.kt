package com.example.diccionario.data

data class Palabra (
    val word: String,
    val meanings: List<Meaning>,
    val ok: Boolean
)