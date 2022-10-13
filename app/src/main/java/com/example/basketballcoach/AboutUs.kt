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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        inicializacionRecyclerView()
    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerIntegrantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = IntegrantesAdapter(listaIntegrantes)
    }
}