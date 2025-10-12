package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoComida.tipocomidalista

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsComidaBebidasBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos
import com.squareup.picasso.Picasso

class TipoComidaListaViewHolder(view: View): RecyclerView.ViewHolder(view){
    val binding = ItemsComidaBebidasBinding.bind(view)
    fun render(alimento: Alimentos, onClickListener: (Alimentos) -> Unit){
        binding.textolista.text = alimento.nombre
        Picasso.get().load(alimento.imagen).into(binding.image) // <- URL completa
        itemView.setOnClickListener{onClickListener(alimento)}
    }
}
