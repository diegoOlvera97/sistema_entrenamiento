package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios

import android.content.DialogInterface.OnClickListener
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo

class EjerciciosAdapter (private val sem:List<ParteTrabajoCuerpo>, private val onClickListener:(ParteTrabajoCuerpo)-> Unit):
    RecyclerView.Adapter<EjerciciosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EjerciciosViewHolder(layoutInflater.inflate(R.layout.items_card_parte_trajada,parent,false))
    }

    override fun getItemCount() = sem.size

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val item = sem[position]
        holder.render(item, onClickListener)
    }

}

