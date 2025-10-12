package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsAlimentosDiasBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion
import com.squareup.picasso.Picasso

class AlimentosViewHolder(view:View):RecyclerView.ViewHolder(view){
    val binding = ItemsAlimentosDiasBinding.bind(view)

    fun render(comida:Alimentos, colacion:Colacion, ruta:String, onClickListener: (Alimentos)->Unit, onClickListenerButon: (Colacion) -> Unit){
        binding.textoCard.text = comida.nombre
        binding.boton.text = colacion.nombre
        Picasso.get().load(ruta + comida.imagen).into(binding.imagen)
        itemView.setOnClickListener{onClickListener(comida)}
        binding.boton.setOnClickListener{onClickListenerButon(colacion)}
    }
}