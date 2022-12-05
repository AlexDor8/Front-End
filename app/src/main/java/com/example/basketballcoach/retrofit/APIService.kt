package com.example.basketballcoach.retrofit

import com.example.basketballcoach.model.Integrantes
import com.example.basketballcoach.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.util.*

interface APIService {

    @GET
    suspend fun getUsuarios(@Url url: String): Response<List<Integrantes>>

    @POST
    suspend fun postRegister(@Body nombre:String, @Body contraseña:String, @Body correo: String, @Body fechaNacimiento:Date):Response<Usuario>
}