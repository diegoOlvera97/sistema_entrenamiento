package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoComida.tipocomidalista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos

class TipoComidaListaAdapter(
    private val list: List<Alimentos>,
    private val onClickListener: (Alimentos) -> Unit
) : RecyclerView.Adapter<TipoComidaListaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoComidaListaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TipoComidaListaViewHolder(layoutInflater.inflate(R.layout.items_comida_bebidas, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TipoComidaListaViewHolder, position: Int) {
        holder.render(list[position], onClickListener)
    }
}
