package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapterTeam.JugadoresAdapter
import com.example.basketballcoach.adapterTeams.EquiposAdapter
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Jugador

class Equipos : AppCompatActivity() {

    var listaEquipos = mutableListOf<Equipo>(
        Equipo(1, "Golden State Warriors",1, ""),
        Equipo(2, "Phoenix Suns",1, ""),
        Equipo(3, "Orlando Magic",1, "")
    )

    private lateinit var equiposRvAdapter: EquiposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        inicializacionRecyclerView()

    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerEquipos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        equiposRvAdapter = EquiposAdapter(listaEquipos)
        recyclerView.adapter = equiposRvAdapter

    }
}