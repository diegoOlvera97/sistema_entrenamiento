package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario.objetivos

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo

class CuestionarioObjetivosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val checkBox: CheckBox = view.findViewById(R.id.checkbox_objetivo)
    private val textView: TextView = view.findViewById(R.id.text_objetivo)

    fun render(item: ParteTrabajoCuerpo, onCheckedChange: (Boolean) -> Unit) {
        textView.text = item.parteCuerpo
        checkBox.isChecked = item.isChecked

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(isChecked)
        }
    }
}
