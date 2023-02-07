package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.basketballcoach.model.LoginInformation
import com.example.basketballcoach.model.Usuario
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var profileName = intent.getStringExtra("nombre").toString()
        var profilePassword = intent.getStringExtra("contrasena").toString()
        conexion(profileName, profilePassword)

    }

    fun conexion(nombre: String, contraseña: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getUserData("baloncesto/Login", LoginInformation(nombre, contraseña))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    var usuario = respuesta.body()
                    val nombreA = findViewById<EditText>(R.id.nombreUsuario)
                    val emailA = findViewById<EditText>(R.id.emailTexto)
                    if (usuario != null) {
                        nombreA.setText(usuario.nombre)
                        emailA.setText(usuario.correo)
                    }

                }else {
                    respuesta.errorBody()?.string()
                }
            }
        }
    }
}