package com.example.basketballcoach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast

class Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val calendar:CalendarView = findViewById(R.id.calendarView);
        
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val msg = "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this@Calendar, msg, Toast.LENGTH_SHORT).show()

        }
            
        }
     }



