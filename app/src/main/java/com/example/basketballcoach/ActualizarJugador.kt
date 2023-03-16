package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.example.basketballcoach.model.Jugador


class ActualizarJugador : AppCompatActivity() {

    lateinit var nombreActualizarJugador: EditText
    lateinit var apellidoActualizarJugador: EditText
    lateinit var posicionActualizarJugador: EditText
    lateinit var dorsalActualizarJugador: EditText
    lateinit var rolActualizarJugador: EditText
    lateinit var saludActualizarJugador: EditText
    lateinit var alturaActualizarJugador: EditText
    lateinit var manoDominanteActualizarJugador: EditText
    lateinit var actulizarImagen: ImageView;
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

        nombreActualizarJugador = findViewById(R.id.nombreJugadorActualizar)
        apellidoActualizarJugador = findViewById<EditText>(R.id.apellidoJugadorActualizar)
        posicionActualizarJugador = findViewById<EditText>(R.id.posicionJugadorActualizar)
        dorsalActualizarJugador = findViewById<EditText>(R.id.dorsalActualizar)
        rolActualizarJugador = findViewById<EditText>(R.id.rolActualizar)
        saludActualizarJugador = findViewById<EditText>(R.id.saludActualizar)
        alturaActualizarJugador = findViewById<EditText>(R.id.alturaActualizar)
        manoDominanteActualizarJugador = findViewById<EditText>(R.id.manoDominanteActualizar)
        actulizarImagen = findViewById<ImageView>(R.id.imagenJugadorActualizar)

        buttonActNom = findViewById<ImageButton>(R.id.buttonActualizarJugador)
        buttonActApe = findViewById<ImageButton>(R.id.buttonActualizarJugadorApellido)
        buttonActPos = findViewById<ImageButton>(R.id.buttonActualizarJugadorPosicion)
        buttonActDor = findViewById<ImageButton>(R.id.buttonActualizarJugadorDorsal)
        buttonActRol = findViewById<ImageButton>(R.id.buttonActualizarJugadorRol)
        buttonActSal = findViewById<ImageButton>(R.id.buttonActualizarJugadorSalud)
        buttonActAlt = findViewById<ImageButton>(R.id.buttonActualizarJugadorAltura)
        buttonActManDom = findViewById<ImageButton>(R.id.buttonActualizarJugadorManoDominante)

        setData()

    }

    fun setData() {
        val jugador: Jugador = intent.getSerializableExtra("jugadorActualizar") as Jugador

        nombreActualizarJugador.setText(jugador.nombre)
        apellidoActualizarJugador.setText(jugador.apellido)
        posicionActualizarJugador.setText(jugador.posicion)
        dorsalActualizarJugador.setText(jugador.dorsal.toString())
        rolActualizarJugador.setText(jugador.rol)
        saludActualizarJugador.setText(jugador.salud)
        alturaActualizarJugador.setText(jugador.altura.toString())
        manoDominanteActualizarJugador.setText(jugador.manoDominante)
    }
}