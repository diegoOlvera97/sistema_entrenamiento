package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio


class TodosLosEjercicioAdapter(
    private val ejer: List<Ejercicio>,
    private val onClickListener: (Ejercicio) -> Unit
) : RecyclerView.Adapter<TodosLosEjercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosLosEjercicioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodosLosEjercicioViewHolder(
            layoutInflater.inflate(R.layout.items_ejercicios_todos, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodosLosEjercicioViewHolder, position: Int) {
        val item = ejer[position]
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide)
        holder.render(item, onClickListener)
        holder.binding.tarjeta.startAnimation(animation)
    }

    override fun getItemCount(): Int = ejer.size
}
