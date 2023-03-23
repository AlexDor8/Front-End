package com.example.basketballcoach.adapterTeams

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basketballcoach.R
import com.example.basketballcoach.model.Equipo
import com.example.basketballcoach.model.Jugador

class EquiposViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val fotoEquipo = view.findViewById<ImageView>(R.id.fotoEquipoRV)
    val nombreEquipo = view.findViewById<TextView>(R.id.nombreEquipoRV)

    fun itemPorListado(equipo: Equipo, onClickListener: (Equipo) -> Unit) {
        nombreEquipo.text = equipo.nombre
        Glide.with(fotoEquipo.context).load(equipo.foto).into(fotoEquipo)
        itemView.setOnClickListener {
            onClickListener(equipo)
        }
    }

}