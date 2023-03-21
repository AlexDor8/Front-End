package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class Menu : AppCompatActivity() {

    lateinit var menuEditar:ImageView
    lateinit var menuCalendario:ImageView
    lateinit var menuEstadisticas:ImageView
    lateinit var menuPizarra:ImageView
    lateinit var menuJugadores:ImageView
    lateinit var menuAlineacion:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        menuEditar = findViewById(R.id.circuloEditar)
        menuAlineacion = findViewById(R.id.circuloAlineacion)
        menuCalendario = findViewById(R.id.circuloCalendario)
        menuEstadisticas = findViewById(R.id.circuloEstadisticas)
        menuPizarra = findViewById(R.id.circuloPizarra)
        menuJugadores = findViewById(R.id.circuloJugadores)

        menuEditar.setOnClickListener {
            val intent: Intent = Intent(this, ActualizarEquipo::class.java);
            startActivity(intent);
        }

        menuCalendario.setOnClickListener {
            val intent: Intent = Intent(this, Calendar::class.java);
            startActivity(intent);
        }

        menuJugadores.setOnClickListener {
            val intent: Intent = Intent(this, Jugadores::class.java);
            startActivity(intent);
        }
    }
}