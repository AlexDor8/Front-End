package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login()

        val register: Button = findViewById(R.id.botonregistro);
        val aboutus: Button = findViewById(R.id.botonaboutus);


        register.setOnClickListener {
            val intent: Intent = Intent(this, Register::class.java);
            startActivity(intent);
        }

        aboutus.setOnClickListener {
            val intent: Intent = Intent(this, AboutUs::class.java);
            startActivity(intent);
        }
    }

    private fun login() {
        var username: TextView = findViewById(R.id.username);
        var password: TextView = findViewById(R.id.password);
        val login: Button = findViewById(R.id.botonlogin);

        var nombreUsuario = username.text.toString()
        var contraseña = password.text.toString()

        login.setOnClickListener {
            conexion(nombreUsuario, contraseña)
        }
    }

    private fun conexion(nombreUsuario: String, contrasena: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).client(client).build()
            var respuesta = conexion.create(APIService::class.java).postLogin("baloncesto/Login", nombreUsuario, contrasena)
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val usuario = respuesta.body()
                    println(usuario)
                }else {
                    respuesta.errorBody()?.string()
                    println("Error al loguearte. El usuario o la contraseña son incorrectos.")
                }
            }
        }
    }
}