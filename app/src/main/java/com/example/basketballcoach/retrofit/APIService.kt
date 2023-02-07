package com.example.basketballcoach.retrofit

import android.util.JsonReader
import com.example.basketballcoach.model.*
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
    suspend fun postRegister(@Url url: String, @Body usuario: Usuario): Response<Feedback>

    @POST
    suspend fun postLogin(@Url url: String, @Body loginInformation: LoginInformation): Response<Boolean>

    @POST
    suspend fun userExist(@Url url: String, @Body usuario: Usuario) : Response<Boolean>

    @POST
    suspend fun mailExist(@Url url: String, @Body mail: String) : Response<Boolean>

    @POST
    suspend fun getUserData(@Url url: String, @Body loginInformation: LoginInformation): Response<Usuario>
}