package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas.graficasEjercicios

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizadoLista

class EstadisticasEjercicioAdapter(
    private val ejercicioRealizadoLista: List<EjercicioRealizadoLista>
) : RecyclerView.Adapter<EstadisticasEjercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstadisticasEjercicioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.items_tasblas_por_ejercicio, parent, false)
        return EstadisticasEjercicioViewHolder(view)
    }

    override fun getItemCount(): Int = ejercicioRealizadoLista.size

    override fun onBindViewHolder(holder: EstadisticasEjercicioViewHolder, position: Int) {
        val item = ejercicioRealizadoLista[position]
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide)
        holder.render(item)
        holder.binding.tarjeta.startAnimation(animation)
    }
}
