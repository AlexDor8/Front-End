package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import retrofit2.create

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun postRegisterUsuario(view: View) {
        val registerNombreUsuario = findViewById<EditText>(R.id.nombreUsuarioRegister)
        val password = findViewById<EditText>(R.id.passwordRegister)
        val repiteContrasena = findViewById<EditText>(R.id.repiteContraseña)
        val email = findViewById<EditText>(R.id.email)
        val fechaNacimiento = findViewById<EditText>(R.id.fechaNacimiento)

        val nombreUsuario = registerNombreUsuario.text.toString()
        val contrasena = password.text.toString()
        val repitePassword = repiteContrasena.text.toString()
        val mail = email.text.toString()
        val nacimiento = fechaNacimiento.text.toString()


        if (contrasena == repitePassword) {
            CoroutineScope(Dispatchers.IO).launch {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
                val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                    .addConverterFactory(
                    GsonConverterFactory.create()).client(client).build()
                var respuesta = conexion.create(APIService::class.java)
                    .postRegister("baloncesto/CrearUsuario", Usuario(0, nombreUsuario, contrasena, mail, nacimiento) )
                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful) {
                        println(respuesta.body())
                    }else {
                        respuesta.errorBody()?.string()
                    }
                }

            }
        }
    }
}