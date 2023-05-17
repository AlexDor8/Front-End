package com.example.basketballcoach

import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast

class Pizarra : AppCompatActivity() {

    companion object {
        var path = Path()
        var paintBrush = Paint()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizarra)

        val redBtn = findViewById<ImageButton>(R.id.color_rojo)
        val blueBtn = findViewById<ImageButton>(R.id.color_azul)
        val blackBtn = findViewById<ImageButton>(R.id.color_negro)
        val eraser = findViewById<ImageButton>(R.id.color_blanco)

        redBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Color rojo seleccionado", Toast.LENGTH_LONG).show();
        }

        blueBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Color azul seleccionado", Toast.LENGTH_LONG).show();
        }

        blackBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Color negro seleccionado", Toast.LENGTH_LONG).show();

        }

        eraser.setOnClickListener {
            Toast.makeText(applicationContext, "Táctica borrada", Toast.LENGTH_LONG).show();

        }

    }
}