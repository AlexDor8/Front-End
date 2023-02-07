package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.basketballcoach.model.LoginInformation
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


        val login: Button = findViewById(R.id.botonlogin);
        val register: Button = findViewById(R.id.botonregistro);
        val aboutus: Button = findViewById(R.id.botonaboutus);

        val username: TextView = findViewById(R.id.username);
        val password: TextView = findViewById(R.id.password);




        login.setOnClickListener {
            val nombre: String = username.text.toString()
            val contraseña: String = password.text.toString()
            if (nombre.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(applicationContext, "Los campos están vacíos", Toast.LENGTH_LONG).show();
            }else {
                conexion(nombre, contraseña)
            }
        }

        register.setOnClickListener {
            val intent: Intent = Intent(this, Register::class.java);
            startActivity(intent);
        }

        aboutus.setOnClickListener {
            val intent: Intent = Intent(this, AboutUs::class.java);
            startActivity(intent);
        }
    }

    private fun conexion(nombre: String, contrasena: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).client(client).build()
            var respuesta = conexion.create(APIService::class.java).postLogin("baloncesto/cLogin", LoginInformation(nombre, contrasena))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    if(respuesta.body() == true) {
                        println("Te has logueado con exito")
                        Toast.makeText(applicationContext, "Te has logueado con éxito!", Toast.LENGTH_LONG).show();
                        val intent: Intent = Intent(this@MainActivity, Profile::class.java);
                        intent.putExtra("nombre", nombre)
                        intent.putExtra("contrasena", contrasena)
                        startActivity(intent);
                    }else{
                        Toast.makeText(applicationContext, "Error al loguearte.", Toast.LENGTH_LONG).show();
                        println("Error al loguearte. El usuario o la contraseña son incorrectos.")
                    }
                }else {
                    respuesta.errorBody()?.string()

                }
            }
        }
    }

    private fun datosUsuario() {
    }
}