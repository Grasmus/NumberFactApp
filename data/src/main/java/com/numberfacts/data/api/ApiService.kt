package com.numberfacts.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{number}")
    suspend fun getNumberFact(
        @Path("number") number: Int
    ): String

    @GET("random/math")
    suspend fun getRandomNumberFact(): String
}
