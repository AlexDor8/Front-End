package com.example.basketballcoach.adapterTeams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.R
import com.example.basketballcoach.model.Equipo


class EquiposAdapter(
    val listEquipos: List<Equipo>,
    private val onClickListener: (Equipo) -> Unit,
    private val onClickDeleted: (Int) -> Unit
) :
    RecyclerView.Adapter<EquiposViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquiposViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EquiposViewHolder(layoutInflater.inflate(R.layout.item_equipo, parent, false))
    }

    override fun onBindViewHolder(holder: EquiposViewHolder, position: Int) {
        val item = listEquipos[position]
        holder.itemPorListado(item, onClickListener, onClickDeleted)
    }

    override fun getItemCount(): Int {
        return listEquipos.size
    }
}