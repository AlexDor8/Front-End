package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter
import com.example.basketballcoach.adapterTeam.JugadoresAdapter
import com.example.basketballcoach.model.Equipo
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

class Jugadores : AppCompatActivity() {

    var listaJugadores = mutableListOf<Jugador>(
        Jugador(
            1,
            "Stephen",
            "Curry",
            "Base",
            30,
            "Jugador franquicia",
            "Sano",
            191,
            "Derecha",
            "",
            Equipo(1, "Golden State Warriors", 1, "")
        ),
        Jugador(
            2,
            "Klay",
            "Thompson",
            "Escolta",
            11,
            "Titular",
            "Sano",
            201,
            "Derecha",
            "",
            Equipo(1, "Golden State Warriors", 1, "")
        )
    )

    private lateinit var jugadoresRvAdapter: JugadoresAdapter

    lateinit var bottomNav : BottomNavigationView
    lateinit var buttonJugador: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugadores)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        buttonJugador = findViewById(R.id.nuevoJugador)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.iconoJugador -> {
                    //loadFragment(PerfilFragment())

                    val intent: Intent = Intent(this@Jugadores, Profile::class.java);
                    startActivity(intent);
                    true
                }
                R.id.iconoJugadores -> {
                    startActivity(intent)
                    //loadFragment(EquipoFragment())
                    true
                }
                R.id.iconoPista -> {
                    //loadFragment(PistaFragment())
                    true
                }
                R.id.iconoCalendario -> {
                    val intent: Intent = Intent(this, Calendar::class.java);
                    startActivity(intent);
                    true
                }
                else -> {
                    println("Clica alguno de los iconos del menu")
                    true
                }
            }

        }

        buttonJugador.setOnClickListener {
            val intent: Intent = Intent(this, AnadirJugador::class.java);
            startActivity(intent);
        }


        inicializacionRecyclerView()
        conexion()
        buscarSearchView()
    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerJugadores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jugadoresRvAdapter = JugadoresAdapter(listaJugadores) { onItemSelected(it) }
        recyclerView.adapter = jugadoresRvAdapter

    }

    private fun onItemSelected(jugador: Jugador) {
        Toast.makeText(applicationContext, jugador.nombre, Toast.LENGTH_LONG).show();
        val intent: Intent = Intent(this, ActualizarJugador::class.java);
        intent.putExtra("jugadorActualizar", jugador)
        startActivity(intent);
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(
                GsonConverterFactory.create()).client(client).build()
            var respuesta = conexion.create(APIService::class.java).getJugadores("baloncesto/getJugadores/1")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadores.clear();
                    listaJugadores.addAll(nuevosJugadores)
                    jugadoresRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun buscarSearchView() {
        var searchView = findViewById<SearchView>(R.id.searchViewJugadores);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query?.isNullOrEmpty()!!) {
                    getJugadoresFiltroNombre(query);
                }
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true;
            }
        }

        )
    }

    private fun getJugadoresFiltroNombre(query:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            var respuesta = conexion.create(APIService::class.java)
                .getJugadores("baloncesto/buscarJugador/$query/1")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosJugadores = respuesta.body() ?: emptyList()
                    listaJugadores.clear()
                    listaJugadores.addAll(nuevosJugadores)
                    jugadoresRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}