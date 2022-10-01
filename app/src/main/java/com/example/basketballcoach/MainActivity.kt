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
        val login: Button = findViewById(R.id.botonlogin);
        val register: Button = findViewById(R.id.botonregistro);

        register.setOnClickListener {
            val intent: Intent = Intent(this,Register::class.java);
            startActivity(intent);
        }
    }
}