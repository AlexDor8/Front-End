package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton


class ActualizarJugador : AppCompatActivity() {

    lateinit var nombreActualizarJugador: EditText
    lateinit var apellidoActualizarJugador: EditText
    lateinit var posicionActualizarJugador: EditText
    lateinit var dorsalActualizarJugador: EditText
    lateinit var rolActualizarJugador: EditText
    lateinit var saludActualizarJugador: EditText
    lateinit var alturaActualizarJugador: EditText
    lateinit var manoDominanteActualizarJugador: EditText
    lateinit var buttonActNom: ImageButton
    lateinit var buttonActApe: ImageButton
    lateinit var buttonActPos: ImageButton
    lateinit var buttonActDor: ImageButton
    lateinit var buttonActRol: ImageButton
    lateinit var buttonActSal: ImageButton
    lateinit var buttonActAlt: ImageButton
    lateinit var buttonActManDom: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_jugador)

        nombreActualizarJugador.findViewById<EditText>(R.id.nombreJugadorActualizar)
        apellidoActualizarJugador.findViewById<EditText>(R.id.apellidoJugadorActualizar)
        posicionActualizarJugador.findViewById<EditText>(R.id.posicionJugadorActualizar)
        dorsalActualizarJugador.findViewById<EditText>(R.id.dorsalActualizar)
        rolActualizarJugador.findViewById<EditText>(R.id.rolActualizar)
        saludActualizarJugador.findViewById<EditText>(R.id.saludActualizar)
        alturaActualizarJugador.findViewById<EditText>(R.id.alturaActualizar)
        manoDominanteActualizarJugador.findViewById<EditText>(R.id.manoDominanteActualizar)

        buttonActNom.findViewById<ImageButton>(R.id.buttonActualizarJugador)
        buttonActApe.findViewById<ImageButton>(R.id.buttonActualizarJugadorApellido)
        buttonActPos.findViewById<ImageButton>(R.id.buttonActualizarJugadorPosicion)

    }
}