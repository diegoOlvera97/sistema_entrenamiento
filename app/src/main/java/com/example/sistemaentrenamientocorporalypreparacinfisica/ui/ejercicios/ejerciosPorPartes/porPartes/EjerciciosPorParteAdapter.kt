package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes.porPartes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio

class EjerciciosPorParteAdapter(
    private val ejer: List<Ejercicio>,
    private val onClickListener: (Ejercicio) -> Unit
) : RecyclerView.Adapter<EjerciciosPorParteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosPorParteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EjerciciosPorParteViewHolder(
            layoutInflater.inflate(R.layout.items_ejercicios_lista, parent, false)
        )
    }

    override fun getItemCount(): Int = ejer.size

    override fun onBindViewHolder(holder: EjerciciosPorParteViewHolder, position: Int) {
        val item = ejer[position]
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide)
        holder.render(item, onClickListener)
        holder.binding.tarjeta.startAnimation(animation)
    }
}
