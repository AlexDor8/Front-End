package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter
import com.example.basketballcoach.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class AboutUs : AppCompatActivity() {

    var listaIntegrantes = mutableListOf<Integrantes>(
        Integrantes(
            nombreApellidos = "Alejandro Dorado Casado",
            especializacion = "Front End",
            informacionIntegrante = "Gran pasión por la programación \ny por el arte digital.",
            foto = R.drawable.foto_alex
        ),
        Integrantes(
            nombreApellidos = "Kilian Herrada Fernández",
            especializacion = "Back End",
            informacionIntegrante = "Interés por el código abierto y por \nmejorar como programador.",
            foto = R.drawable.foto_kilian
        ),
        Integrantes(
            nombreApellidos = "Tigé David Ral Ramirez",
            especializacion = "Informe técnico",
            informacionIntegrante = "Me gusta expresar mi creatividad \na través del desarrollo de " +
                    "software.\nY me satisface resolver problemas\nlógicos.",
            foto = R.drawable.foto_tige
        )
    )
    val recyclerView = findViewById<RecyclerView>(R.id.recyclerIntegrantes)
    private lateinit var integrantesRvAdapter: IntegrantesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        inicializacionRecyclerView()
    }

    private fun inicializacionRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        integrantesRvAdapter = IntegrantesAdapter(listaIntegrantes)
        recyclerView.adapter = integrantesRvAdapter
    }

    private fun conexion() {
        lifecycleScope.launch(Dispatchers.Default) {
            val conexion = Retrofit.Builder().baseUrl("http://10.0.2.2:80/").addConverterFactory(GsonConverterFactory.create()).build()
            withContext(Dispatchers.Main) {
                var respuesta = conexion.create(APIService::class.java).getUsuarios("../php/integrantesGET.php")
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