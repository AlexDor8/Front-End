package com.example.basketballcoach.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.model.Integrantes
import com.example.basketballcoach.R

class IntegrantesAdapter(val listaIntegrantes: List<Integrantes>) : RecyclerView.Adapter<IntegrantesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegrantesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IntegrantesViewHolder(layoutInflater.inflate(R.layout.item_integrante, parent, false))
    }

    override fun onBindViewHolder(holder: IntegrantesViewHolder, position: Int) {
        val item = listaIntegrantes[position]
        holder.itemPorListado(item)

    }

    override fun getItemCount(): Int {
        return listaIntegrantes.size;
    }
}