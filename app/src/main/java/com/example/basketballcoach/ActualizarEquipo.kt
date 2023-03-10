package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class ActualizarEquipo : AppCompatActivity() {

    lateinit var actualizarEquipoNombre:EditText
    lateinit var imagenEquipoActualizar:ImageView
    lateinit var botonActualizarNombre:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_equipo)

        actualizarEquipoNombre.findViewById<EditText>(R.id.nombreEquipoActualizar)
        imagenEquipoActualizar.findViewById<ImageView>(R.id.imagenEquipoActualizar)
        botonActualizarNombre.findViewById<ImageButton>(R.id.nombreEquipoActualizar)
    }
}