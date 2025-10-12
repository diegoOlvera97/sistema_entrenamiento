package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoColacion.tipocolacionlista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion

class TipoColacionListaAdapter(
    private val list: List<Colacion>,
    private val onClickListener: (Colacion) -> Unit
) : RecyclerView.Adapter<TipoColacionListaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoColacionListaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TipoColacionListaViewHolder(layoutInflater.inflate(R.layout.items_comida_bebidas, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TipoColacionListaViewHolder, position: Int) {
        holder.render(list[position], onClickListener)
    }
}
