package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio

class Ejercicio_actividad_adapter(
    private val ejer: List<Ejercicio>,
    private val reps: List<Float>,
    private val onClickListener: (Ejercicio) -> Unit
) : RecyclerView.Adapter<Ejercicio_actividad_viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Ejercicio_actividad_viewholder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Ejercicio_actividad_viewholder(
            layoutInflater.inflate(R.layout.items_ejercicios_lista_reps, parent, false)
        )
    }

    override fun getItemCount(): Int = ejer.size

    override fun onBindViewHolder(holder: Ejercicio_actividad_viewholder, position: Int) {
        val item = ejer[position]
        val reps = reps[position]
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide)
        holder.binding.tarjeta.startAnimation(animation)
        holder.render(item, reps, onClickListener)
    }
}



/*
* class EjerciciosAdapter (private val sem:List<String>,private val onClickListener:(String)-> Unit):
    RecyclerView.Adapter<EjerciciosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EjerciciosViewHolder(layoutInflater.inflate(R.layout.items_card_rutinas,parent,false))
    }

    override fun getItemCount(): Int {
        return sem.size
    }

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val item = sem[position]
        holder.render(item, onClickListener)
    }

}
* */