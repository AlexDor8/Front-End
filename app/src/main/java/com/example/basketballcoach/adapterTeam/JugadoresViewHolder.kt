package com.example.basketballcoach.adapterTeam

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basketballcoach.R
import com.example.basketballcoach.model.Jugador

class JugadoresViewHolder(view: View):RecyclerView.ViewHolder(view) {

    val fotoJugador = view.findViewById<ImageView>(R.id.fotoJugadorRV)
    val nombreJugador = view.findViewById<TextView>(R.id.nombreJugadorRV)

    fun itemPorListado(jugador: Jugador) {
        nombreJugador.text = jugador.nombre + " " + jugador.primerApellido
        Glide.with(fotoJugador.context).load(jugador.foto).into(fotoJugador)
    }

}