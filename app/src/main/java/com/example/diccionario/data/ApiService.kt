package com.example.diccionario.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Obtener una palabra concreta
    @GET("words/{word}")
    suspend fun getWord(
        @Path("word") word: String
    ): PalabraResponse
}