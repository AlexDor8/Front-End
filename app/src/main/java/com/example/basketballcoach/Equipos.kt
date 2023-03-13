package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Jugador

class Equipos : AppCompatActivity() {

    var listaEquipos = mutableListOf<Equipo>(
        Equipo(1, "Golden State Warriors",1, ""),
        Equipo(2, "Phoenix Suns",1, ""),
        Equipo(3, "Orlando Magic",1, "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)
    }
}