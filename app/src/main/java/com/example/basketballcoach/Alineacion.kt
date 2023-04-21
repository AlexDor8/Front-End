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

    var listaJugadoresNombreEscolta = mutableListOf<String>(
        "SG"
    )

    var listaJugadoresNombreAlaPivot = mutableListOf<String>(
        "PF"
    )

    var listaJugadoresNombrePivot = mutableListOf<String>(
        "C"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alineacion)

        conexion()
        conexionAlero()
        conexionEscolta()
        conexionAlaPivot()
        conexionPivot()

        confSpinnerBase()
        confSpinnerAlero()
        confSpinnerEscolta()
        confSpinnerAlapivot()
        confSpinnerPivot()

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

    private fun confSpinnerEscolta() {
        val spinnerEscolta = findViewById<Spinner>(R.id.spinnerEscolta)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadoresNombreEscolta)

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerEscolta.adapter = adaptador
    }

    private fun confSpinnerAlapivot() {
        val spinnerAlapivot = findViewById<Spinner>(R.id.spinnerAlapivot)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadoresNombreAlaPivot)

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerAlapivot.adapter = adaptador
    }

    private fun confSpinnerPivot() {
        val spinnerPivot = findViewById<Spinner>(R.id.spinnerPivot)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadoresNombrePivot)

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerPivot.adapter = adaptador
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

    private fun conexionEscolta() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getNombresPosicion("baloncesto/buscarNombrePosicion/${Globals.equipo.id}/Escolta")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadoresNombreEscolta.clear();
                    listaJugadoresNombreEscolta.addAll(nuevosJugadores)
                    println(listaJugadoresNombreEscolta)
                }
            }
        }
    }

    private fun conexionAlaPivot() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getNombresPosicion("baloncesto/buscarNombrePosicion/${Globals.equipo.id}/Ala-pivot")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadoresNombreAlaPivot.clear();
                    listaJugadoresNombreAlaPivot.addAll(nuevosJugadores)
                    println(listaJugadoresNombreAlaPivot)
                }
            }
        }
    }

    private fun conexionPivot() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getNombresPosicion("baloncesto/buscarNombrePosicion/${Globals.equipo.id}/Pivot")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadoresNombrePivot.clear();
                    listaJugadoresNombrePivot.addAll(nuevosJugadores)
                    println(listaJugadoresNombrePivot)
                }
            }
        }
    }
}