package com.example.basketballcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var username: TextView = findViewById(R.id.username);
        var password: TextView = findViewById(R.id.password);

        data class Usuario(var username: TextView, var password: TextView);

        val login: Button = findViewById(R.id.botonlogin);
        val register: Button = findViewById(R.id.botonregistro);
        val aboutus: Button = findViewById(R.id.botonaboutus);


        register.setOnClickListener {
            val intent: Intent = Intent(this,Register::class.java);
            startActivity(intent);
        }

        aboutus.setOnClickListener {
            val intent: Intent = Intent(this,AboutUs::class.java);
            startActivity(intent);
        }
    }
}