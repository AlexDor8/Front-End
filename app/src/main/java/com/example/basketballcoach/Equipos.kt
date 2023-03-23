package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapterTeam.JugadoresAdapter
import com.example.basketballcoach.adapterTeams.EquiposAdapter
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

class Equipos : AppCompatActivity() {

    var listaEquipos = mutableListOf<Equipo>(
        Equipo(1, "Golden State Warriors",1, ""),
        Equipo(2, "Phoenix Suns",1, ""),
        Equipo(3, "Orlando Magic",1, "")
    )

    private lateinit var equiposRvAdapter: EquiposAdapter

    lateinit var buttonEquipo: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        buttonEquipo = findViewById(R.id.nuevoEquipo)

        buttonEquipo.setOnClickListener {
            val intent: Intent = Intent(this, AnadirEquipo::class.java);
            startActivity(intent);
        }

        inicializacionRecyclerView()

        conexion()

    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEquipos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        equiposRvAdapter = EquiposAdapter(listaEquipos) { onItemSelected(it) }
        recyclerView.adapter = equiposRvAdapter
    }

    private fun onItemSelected(equipo: Equipo) {
        Toast.makeText(applicationContext, equipo.nombre, Toast.LENGTH_LONG).show();
        val intent: Intent = Intent(this, Menu::class.java);
        intent.putExtra("menuEquipo", equipo)
        startActivity(intent);
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getEquipos("baloncesto/getEquipos/${Globals.usuario.id}")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosEquipos = respuesta.body() ?: emptyList()
                    listaEquipos.clear();
                    listaEquipos.addAll(nuevosEquipos)
                    equiposRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}