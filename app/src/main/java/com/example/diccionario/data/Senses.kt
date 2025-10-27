package com.example.diccionario.data

data class Senses(
    val category: String = "",
    val description: String = "",
    val synonyms: List<String>? = emptyList(),
    val antonyms: List<String>? = emptyList()
)
