package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var integrantesRvAdapter: IntegrantesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        inicializacionRecyclerView()
        conexion()
        buscarSearchView()
    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerIntegrantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        integrantesRvAdapter = IntegrantesAdapter(listaIntegrantes)
        recyclerView.adapter = integrantesRvAdapter
    }

    private fun conexion() {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(GsonConverterFactory.create()).build()
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
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:8081/").addConverterFactory(GsonConverterFactory.create()).build()
            var respuesta = conexion.create(APIService::class.java).getUsuarios("baloncesto/Integrantes/$query")
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