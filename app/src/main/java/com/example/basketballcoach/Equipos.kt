package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapterTeam.JugadoresAdapter
import com.example.basketballcoach.adapterTeams.EquiposAdapter
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Globals
import com.example.basketballcoach.model.Jugador
import com.example.basketballcoach.retrofit.APIService
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        Equipo(1, "Golden State Warriors", 1, ""),
        Equipo(2, "Phoenix Suns", 1, ""),
        Equipo(3, "Orlando Magic", 1, "")
    )

    private lateinit var equiposRvAdapter: EquiposAdapter

    lateinit var buttonEquipo: ImageButton

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.iconoJugador -> {
                    val intent: Intent = Intent(this@Equipos, Profile::class.java);
                    startActivity(intent);
                    //loadFragment(EquipoFragment())
                    true

                }
                R.id.iconoJugadores -> {
                    //loadFragment(PerfilFragment())
                    startActivity(intent)
                    true
                }
                R.id.iconoPista -> {
                    val intent: Intent = Intent(this@Equipos, Pizarra::class.java);
                    startActivity(intent);
                    true
                }
                R.id.iconoCalendario -> {
                    val intent: Intent = Intent(this@Equipos, Calendar::class.java);
                    startActivity(intent);
                    true
                }
                else -> {
                    println("Clica alguno de los iconos del menu")
                    true
                }
            }

        }

        buttonEquipo = findViewById(R.id.nuevoEquipo)

        buttonEquipo.setOnClickListener {
            val intent: Intent = Intent(this, AnadirEquipo::class.java);
            startActivity(intent);
        }

        inicializacionRecyclerView()

        conexion()

        buscarSearchView()

    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEquipos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        equiposRvAdapter = EquiposAdapter(
        listEquipos = listaEquipos,
        onClickListener = { equipo -> onItemSelected(equipo) },
        onClickDeleted = { onDeletedItem(it) }
        )
        recyclerView.adapter = equiposRvAdapter
    }

    private fun onDeletedItem(position: Int) {
        val equipo: Equipo = listaEquipos[position]
        conexionEliminarEquipo(equipo)
        listaEquipos.removeAt(position)
        equiposRvAdapter.notifyItemRemoved(position)
    }

    private fun conexionEliminarEquipo(equipo: Equipo) {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()
            ).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .eliminarEquipo("baloncesto/eliminarEquipo/${equipo.id}")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    println(respuesta.body())
                    Toast.makeText(
                        applicationContext,
                        "El equipo ha sido eliminado con éxito!",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }
        }
    }

    private fun onItemSelected(equipo: Equipo) {
        Toast.makeText(applicationContext, equipo.nombre, Toast.LENGTH_LONG).show();
        Globals.equipo = equipo
        val intent: Intent = Intent(this, Menu::class.java);
        startActivity(intent);
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()
            ).client(client).build()
            var respuesta = conexion.create(APIService::class.java)
                .getEquipos("baloncesto/getEquipos/${Globals.usuario.id}")
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

    private fun buscarSearchView() {
        var searchView = findViewById<SearchView>(R.id.searchViewEquipos);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query?.isNullOrEmpty()!!) {
                    getEquiposFiltroNombre(query);
                }
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true;
            }
        }

        )
    }

    private fun getEquiposFiltroNombre(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            var respuesta = conexion.create(APIService::class.java)
                .getEquipos("baloncesto/buscarEquipo/${Globals.usuario.id}/$query")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosEquipos = respuesta.body() ?: emptyList()
                    listaEquipos.clear()
                    listaEquipos.addAll(nuevosEquipos)
                    equiposRvAdapter.notifyDataSetChanged()
                    println(listaEquipos)
                }
            }
        }
    }
}