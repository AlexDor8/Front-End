package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter
import com.example.basketballcoach.model.Integrantes
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

class AboutUs : AppCompatActivity() {

    var listaIntegrantes = mutableListOf<Integrantes>(
        Integrantes(
            id = 1,
            nombre = "Alejandro Dorado Casado",
            especializacion = "Front End",
            informacion = "Gran pasión por la programación \ny por el arte digital.",
            foto = ""
        ),
        Integrantes(
            id = 2,
            nombre = "Kilian Herrada Fernández",
            especializacion = "Back End",
            informacion = "Interés por el código abierto y por \nmejorar como programador.",
            foto = ""
        ),
        Integrantes(
            id = 3,
            nombre = "Tigé David Ral Ramirez",
            especializacion = "Informe técnico",
            informacion = "Me gusta expresar mi creatividad \na través del desarrollo de " +
                    "software.\nY me satisface resolver problemas\nlógicos.",
            foto = ""
        )
    )

    lateinit var bottomNav : BottomNavigationView

    private lateinit var integrantesRvAdapter: IntegrantesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.iconoJugador -> {
                    //loadFragment(PerfilFragment())
                    val intent: Intent = Intent(this@AboutUs, MainActivity::class.java);
                    startActivity(intent);
                    true
                }
                R.id.iconoJugadores -> {
                    loadFragment(EquipoFragment())
                    true
                }
                R.id.iconoPista -> {
                    loadFragment(PistaFragment())
                    true
                }
                else -> {
                    loadFragment(PistaFragment())
                    true
                }
            }

        }

        inicializacionRecyclerView()
        conexion()
        buscarSearchView()
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerIntegrantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        integrantesRvAdapter = IntegrantesAdapter(listaIntegrantes)
        recyclerView.adapter = integrantesRvAdapter
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(GsonConverterFactory.create()).client(client).build()
                var respuesta = conexion.create(APIService::class.java).getUsuarios("baloncesto/Integrantes")
                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful) {
                        val nuevosIntegrantes = respuesta.body() ?: emptyList()
                        listaIntegrantes.clear();
                        listaIntegrantes.addAll(nuevosIntegrantes)
                        integrantesRvAdapter.notifyDataSetChanged()
                    }
            }
        }
    }

    private fun buscarSearchView() {
        var searchView = findViewById<SearchView>(R.id.searchViewIntegrantes);
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query?.isNullOrEmpty()!!) {
                    getUsuariosFiltroNombre(query);
                }
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true;
            }
        }

        )

    }

    private fun getUsuariosFiltroNombre(query:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            var respuesta = conexion.create(APIService::class.java)
                .getUsuarios("baloncesto/Integrantes/$query")
            withContext(Dispatchers.Main) {
                if (respuesta.isSuccessful) {
                    val nuevosIntegrantes = respuesta.body() ?: emptyList()
                    listaIntegrantes.clear();
                    listaIntegrantes.addAll(nuevosIntegrantes)
                    integrantesRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}