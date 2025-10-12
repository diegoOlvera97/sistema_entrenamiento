package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsEjerciciosTodosBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.squareup.picasso.Picasso

class TodosLosEjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemsEjerciciosTodosBinding.bind(view)

    fun render(ejercicio: Ejercicio, onClickListener: (Ejercicio) -> Unit) {
        binding.textolista.text = ejercicio.nombre
        // Cargar imagen directamente desde la URL de Supabase
        Picasso.get()
            .load(ejercicio.imagen)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_dark)
            .into(binding.image)

        itemView.setOnClickListener { onClickListener(ejercicio) }
    }
}
