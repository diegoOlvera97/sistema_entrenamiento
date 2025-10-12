package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsEjerciciosListaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsEjerciciosListaRepsBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.squareup.picasso.Picasso

class Ejercicio_actividad_viewholder(view:View):RecyclerView.ViewHolder(view){
    val binding = ItemsEjerciciosListaRepsBinding.bind(view)

    fun render(ejercicio: Ejercicio, dato: Float, onClickListener: (Ejercicio) -> Unit) {
        binding.textolista.text = ejercicio.nombre
        binding.description.text = ejercicio.descripcion
        binding.repeticiones.text = "Repeticiones: ${dato.toInt()}"
        Picasso.get().load(ejercicio.imagen).into(binding.imageEjer)
        itemView.setOnClickListener { onClickListener(ejercicio) }
    }


}




/*
* class EjerciciosViewHolder (view:View):RecyclerView.ViewHolder(view){
    val binding = ItemsCardRutinasBinding.bind(view)

    fun render(semana:String, onClickListener: (String)->Unit){
        binding.textoCard.text = semana
        itemView.setOnClickListener{onClickListener(semana)}
    }
}
*
* */