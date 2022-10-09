package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter

class AboutUs : AppCompatActivity() {

    var listaIntegrantes = listOf<Integrantes>(
        Integrantes(
            nombreApellidos = "Alejandro Dorado Casado",
            especializacion = "Front End",
            informacionIntegrante = "Gran pasión por la programación \n y por el arte digital.",
            foto = R.drawable.avatar
        ),
        Integrantes(
            nombreApellidos = "Kilian Herrada Fernandez",
            especializacion = "Back End",
            informacionIntegrante = "Gran pasión por la programación \n y por el arte digital.",
            foto = R.drawable.avatar
        ),
        Integrantes(
            nombreApellidos = "Tige David Ral Ramirez",
            especializacion = "Informe tecnico",
            informacionIntegrante = "Gran pasión por la programación \n y por el arte digital.",
            foto = R.drawable.avatar
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        inicializacionRecyclerView()
    }

    private fun inicializacionRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerIntegrantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter= IntegrantesAdapter(listaIntegrantes)
    }
}