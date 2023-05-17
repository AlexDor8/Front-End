package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Globals
import com.example.basketballcoach.model.Jugador
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnadirJugador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_jugador)

        val nombreJugador = findViewById<EditText>(R.id.nombreJugador)
        val apellidoJugador = findViewById<EditText>(R.id.apellidoJugador)
        val posicionJugador = findViewById<EditText>(R.id.posicionJugador)
        val dorsal = findViewById<EditText>(R.id.dorsal)
        val rol = findViewById<EditText>(R.id.rol)
        val salud = findViewById<EditText>(R.id.salud)
        val altura = findViewById<EditText>(R.id.altura)
        val manoDominante = findViewById<EditText>(R.id.manoDominante)
        val bottonAnadirJugador = findViewById<Button>(R.id.anadirJugador)

        bottonAnadirJugador.setOnClickListener {
            val nJugador = nombreJugador.text.toString()
            val aJugador = apellidoJugador.text.toString()
            val pJugador = posicionJugador.text.toString()
            val dJugadorText = dorsal.text.toString()
            val rJugador = rol.text.toString()
            val sJugador = salud.text.toString()
            val altJugadorText = altura.text.toString()
            val mJugador = manoDominante.text.toString()

            if (nJugador.isEmpty() || aJugador.isEmpty() || pJugador.isEmpty() || dJugadorText.isEmpty() || rJugador.isEmpty() || sJugador.isEmpty() || altJugadorText.isEmpty() || mJugador.isEmpty()) {
                Toast.makeText(applicationContext, "Los campos están vacíos", Toast.LENGTH_LONG)
                    .show();
            } else {
                val dJugador = dorsal.text.toString().toInt()
                val altJugador = altura.text.toString().toInt()

                if (pJugador != "Base" && pJugador != "Escolta" && pJugador != "Alero" && pJugador != "Ala-pivot" && pJugador != "Pivot") {
                    posicionJugador.error =
                        "Introduce una de estas opciones (Base, Escolta, Alero, Ala-pivot, Pivot)"
                } else if (sJugador != "Lesionado" && sJugador != "Sano") {
                    salud.error = "Introduce una de estas opciones (Sano, Lesionado)"
                } else if (mJugador != "Zurdo" && mJugador != "Diestro" && mJugador != "Ambidiestro") {
                    manoDominante.error =
                        "Introduce una de estas opciones (Diestro, Zurdo, Ambidiestro)"
                } else {
                    conexion(
                        nJugador,
                        aJugador,
                        pJugador,
                        dJugador,
                        rJugador, sJugador,
                        altJugador,
                        mJugador
                    )
                }
            }
        }
    }

    fun conexion(
        nombre: String,
        primerApellido: String,
        posicion: String,
        dorsal: Int,
        rol: String,
        salud: String,
        altura: Int,
        manoDominante: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .anadirJugador(
                    "baloncesto/anadirJugador",
                    Jugador(
                        0,
                        nombre,
                        primerApellido,
                        posicion,
                        dorsal,
                        rol,
                        salud,
                        altura,
                        manoDominante,
                        "",
                        Equipo(
                            Globals.equipo.id,
                            Globals.equipo.nombre,
                            Globals.usuario.id,
                            Globals.equipo.foto
                        )
                    )
                )
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(
                        applicationContext,
                        "El jugador ha sido añadido con éxito!",
                        Toast.LENGTH_LONG
                    ).show();
                    val intent: Intent = Intent(this@AnadirJugador, Jugadores::class.java);
                    startActivity(intent);
                } else {
                    respuesta.errorBody()?.string()
                }
            }

        }
    }


}