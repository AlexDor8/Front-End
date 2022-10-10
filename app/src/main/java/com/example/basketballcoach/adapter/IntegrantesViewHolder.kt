package com.example.basketballcoach.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcoach.Integrantes
import com.example.basketballcoach.R

class IntegrantesViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.nombreIntegrante)
    val especializacion = view.findViewById<TextView>(R.id.especializacion)
    val informacion = view.findViewById<TextView>(R.id.descripcion)
    var foto = view.findViewById<ImageView>(R.id.fotoIntegrante)

    fun itemPorListado(integrantes: Integrantes){
        nombre.text = integrantes.nombreApellidos
        especializacion.text = integrantes.especializacion
        informacion.text = integrantes.informacionIntegrante
        foto.setImageResource(integrantes.foto)
    }
}