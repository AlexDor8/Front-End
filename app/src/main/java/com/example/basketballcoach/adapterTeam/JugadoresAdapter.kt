package com.example.basketballcoach.adapterTeam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.R
import com.example.basketballcoach.model.Jugador

class JugadoresAdapter(val listJugadores: List<Jugador>, private val onClickListener:(Jugador) -> Unit): RecyclerView.Adapter<JugadoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadoresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return JugadoresViewHolder(layoutInflater.inflate(R.layout.item_jugador, parent, false))
    }

    override fun onBindViewHolder(holder: JugadoresViewHolder, position: Int) {
        val item = listJugadores[position]
        holder.itemPorListado(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return listJugadores.size;
    }
}

