package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Globals
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnadirEquipo : AppCompatActivity() {

    lateinit var nombreEquipo: EditText
    lateinit var anadirEquipo: Button
    lateinit var imagenEquipo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_equipo)

        nombreEquipo = findViewById<EditText>(R.id.nombreEquipo)
        anadirEquipo = findViewById<Button>(R.id.anadirEquipo)
        imagenEquipo = findViewById<ImageView>(R.id.imagenEquipo)

        anadirEquipo.setOnClickListener {

            var nEquipo = nombreEquipo.text.toString()
            if (nEquipo.isEmpty()) {
                Toast.makeText(applicationContext, "Los campos están vacíos", Toast.LENGTH_LONG).show();
            }else {
                conexion(nEquipo);
            }

        }
    }

    fun conexion(nombreEquipo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .anadirEquipo("baloncesto/anadirEquipo", Equipo(0, nombreEquipo, Globals.usuario.id,""))
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(applicationContext, "El equipo ha sido añadido con éxito!", Toast.LENGTH_LONG).show();
                    val intent: Intent = Intent(this@AnadirEquipo, Equipos::class.java);
                    startActivity(intent);
                }else {
                    respuesta.errorBody()?.string()
                }
            }

        }
    }
}