package com.example.basketballcoach.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basketballcoach.model.Integrantes
import com.example.basketballcoach.R

class IntegrantesViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.nombreIntegrante)
    val especializacion = view.findViewById<TextView>(R.id.especializacion)
    val informacion = view.findViewById<TextView>(R.id.descripcion)
    var fotoIntegrante = view.findViewById<ImageView>(R.id.fotoIntegrante)

    fun itemPorListado(integrantes: Integrantes){
        nombre.text = integrantes.nombre
        especializacion.text = integrantes.especializacion
        informacion.text = integrantes.informacion
        Glide.with(fotoIntegrante.context)
            .load(integrantes.foto)
            .into(fotoIntegrante);
    }
}