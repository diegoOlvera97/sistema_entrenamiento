package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoColacion.tipocolacionlista

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsComidaBebidasBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion
import com.squareup.picasso.Picasso

class TipoColacionListaViewHolder(view: View): RecyclerView.ViewHolder(view){
    val binding = ItemsComidaBebidasBinding.bind(view)

    fun render(colacion: Colacion, onClickListener: (Colacion) -> Unit){
        binding.textolista.text = colacion.nombre
        Picasso.get().load(colacion.imagen).into(binding.image) // <- usar URL completa
        itemView.setOnClickListener{ onClickListener(colacion) }
    }
}
