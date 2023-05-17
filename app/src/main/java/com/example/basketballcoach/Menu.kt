package com.example.basketballcoach

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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

class Menu : AppCompatActivity() {

    lateinit var menuEditar:ImageView
    lateinit var menuCalendario:ImageView
    lateinit var menuEstadisticas:ImageView
    lateinit var menuPizarra:ImageView
    lateinit var menuJugadores:ImageView
    lateinit var menuAlineacion:ImageView

    lateinit var imagenUsuario: ImageView
    lateinit var nombreUsuario: TextView
    lateinit var nombreEquipo: TextView
    lateinit var imagenEquipo: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        menuEditar = findViewById(R.id.circuloEditar)
        menuAlineacion = findViewById(R.id.circuloAlineacion)
        menuCalendario = findViewById(R.id.circuloCalendario)
        menuEstadisticas = findViewById(R.id.circuloEstadisticas)
        menuPizarra = findViewById(R.id.circuloPizarra)
        menuJugadores = findViewById(R.id.circuloJugadores)

        imagenUsuario = findViewById(R.id.menuFotoUsuario)
        nombreUsuario = findViewById(R.id.menuNombreUsuario)
        nombreEquipo = findViewById(R.id.menuNombreEquipo)
        imagenEquipo = findViewById(R.id.menuLogoEquipo)

        //conexionFotoUsuario()

        setData()

        menuEditar.setOnClickListener {
            val intent: Intent = Intent(this, ActualizarEquipo::class.java);
            startActivity(intent);
        }

        menuCalendario.setOnClickListener {
            val intent: Intent = Intent(this, Calendar::class.java);
            startActivity(intent);
        }

        menuJugadores.setOnClickListener {
            val intent: Intent = Intent(this, Jugadores::class.java);
            startActivity(intent);
        }

        menuAlineacion.setOnClickListener {
            val intent: Intent = Intent(this, Alineacion::class.java);
            startActivity(intent);
        }

        menuPizarra.setOnClickListener {
            val intent: Intent = Intent(this, Pizarra::class.java);
            startActivity(intent);
        }


    }

    private fun setData() {
        imagenUsuario.setImageURI(Uri.parse(Globals.usuario.foto))
        nombreUsuario.text = Globals.usuario.nombre
        nombreEquipo.text = Globals.equipo.nombre
        imagenEquipo.setImageURI(Uri.parse(Globals.equipo.foto))
    }

    private fun conexionFotoUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getFoto("baloncesto/getFoto/${Globals.usuario.id}")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val foto = respuesta.body()
                    Globals.usuario.foto = foto.toString()
                }
            }
        }
    }
}