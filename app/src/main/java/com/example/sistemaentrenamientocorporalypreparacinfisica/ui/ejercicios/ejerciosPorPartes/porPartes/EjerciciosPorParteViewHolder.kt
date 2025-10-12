package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes.porPartes

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsEjerciciosListaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.squareup.picasso.Picasso

class EjerciciosPorParteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemsEjerciciosListaBinding.bind(view)

    fun render(ejercicio: Ejercicio, onClickListener: (Ejercicio) -> Unit) {
        binding.textolista.text = ejercicio.nombre
        binding.description.text = ejercicio.descripcion
        // Cargar imagen directamente desde URL completa
        Picasso.get()
            .load(ejercicio.imagen)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_dark)
            .into(binding.imageEjer)

        itemView.setOnClickListener { onClickListener(ejercicio) }
    }
}
