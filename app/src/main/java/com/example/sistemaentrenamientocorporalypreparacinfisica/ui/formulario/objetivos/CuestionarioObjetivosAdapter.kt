package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario.objetivos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo

class CuestionarioObjetivosAdapter(
    private val objetivos: List<ParteTrabajoCuerpo>
) : RecyclerView.Adapter<CuestionarioObjetivosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuestionarioObjetivosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CuestionarioObjetivosViewHolder(
            layoutInflater.inflate(R.layout.items_objetivos, parent, false)
        )
    }

    override fun getItemCount() = objetivos.size

    override fun onBindViewHolder(holder: CuestionarioObjetivosViewHolder, position: Int) {
        val item = objetivos[position]
        holder.render(item) { isChecked ->
            // 🔄 Actualizamos el estado dentro de la lista
            item.isChecked = isChecked
        }
    }
}
