package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.adapter.IntegrantesAdapter
import com.example.basketballcoach.adapterTeam.JugadoresAdapter
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Jugador

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
            Equipo(1, "Golden State Warriors", 1)
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
            Equipo(1, "Golden State Warriors", 1)
        )
    )

    private lateinit var jugadoresRvAdapter: JugadoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugadores)
        inicializacionRecyclerView()
    }

    private fun inicializacionRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerJugadores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jugadoresRvAdapter = JugadoresAdapter(listaJugadores)
        recyclerView.adapter = jugadoresRvAdapter
    }
}