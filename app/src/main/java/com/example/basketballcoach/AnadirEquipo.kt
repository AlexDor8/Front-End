package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class AnadirEquipo : AppCompatActivity() {

    lateinit var nombreEquipo: EditText
    lateinit var anadirEquipo: Button
    lateinit var imagenEquipo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_equipo)

        nombreEquipo.findViewById<EditText>(R.id.nombreEquipo)
        anadirEquipo.findViewById<Button>(R.id.anadirEquipo)
        imagenEquipo.findViewById<ImageView>(R.id.imagenEquipo)
    }
}