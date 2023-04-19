package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
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

class Alineacion : AppCompatActivity() {

    var listaJugadoresNombreBase = mutableListOf<String>(
        "PG"
    )

    var listaJugadoresNombreAlero = mutableListOf<String>(
        "SF"
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alineacion)

        conexion()
        conexionAlero()

        confSpinnerBase()
        confSpinnerAlero()

    }

    private fun confSpinnerBase() {
        val spinnerBase = findViewById<Spinner>(R.id.spinnerBase)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadoresNombreBase)

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerBase.adapter = adaptador

    }

    private fun confSpinnerAlero() {
        val spinnerAlero = findViewById<Spinner>(R.id.spinnerAlero)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadoresNombreAlero)



        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerAlero.adapter = adaptador
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getNombresPosicion("baloncesto/buscarNombrePosicion/${Globals.equipo.id}/Base")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadoresNombreBase.clear();
                    listaJugadoresNombreBase.addAll(nuevosJugadores)
                    println(listaJugadoresNombreBase)
                }
            }
        }
    }

    private fun conexionAlero() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getNombresPosicion("baloncesto/buscarNombrePosicion/${Globals.equipo.id}/Alero")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadoresNombreAlero.clear();
                    listaJugadoresNombreAlero.addAll(nuevosJugadores)
                    println(listaJugadoresNombreAlero)
                }
            }
        }
    }
}