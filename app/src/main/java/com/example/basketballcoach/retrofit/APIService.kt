package com.example.basketballcoach.retrofit

import android.util.JsonReader
import com.example.basketballcoach.model.*
import retrofit2.Response
import retrofit2.http.*
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

    @PUT
    suspend fun editName(@Url url: String, @Body updateUser: UpdateUser) : Response<Feedback>

    @PUT
    suspend fun editEmail(@Url url: String, @Body updateEmail: UpdateEmail) : Response<Feedback>

    @PUT
    suspend fun editFecha(@Url url: String, @Body updateFecha: UpdateFecha) : Response<Feedback>

    @PUT
    suspend fun editContrasena(@Url url: String, @Body updatePassword: UpdatePassword) : Response<Feedback>

    @POST
    suspend fun esContrasena(@Url url: String, @Body updatePassword: UpdatePassword) : Response<Boolean>

}