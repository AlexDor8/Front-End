package com.example.basketballcoach.retrofit

import com.example.basketballcoach.Integrantes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getUsuarios(@Url url: String): Response<List<Integrantes>>
}