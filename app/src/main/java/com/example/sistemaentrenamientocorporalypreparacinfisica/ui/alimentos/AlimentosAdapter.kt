package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion

class AlimentosAdapter(private val alimentos:List<Alimentos>,
                       private val colacion:List<Colacion>,
                       private val ruta:String,
                       private val onClickListener: (Alimentos)->Unit,
                       private val onClickListenerButon: (Colacion) -> Unit):
 RecyclerView.Adapter<AlimentosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlimentosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AlimentosViewHolder(layoutInflater.inflate(R.layout.items_alimentos_dias,parent,false))
    }

    override fun getItemCount(): Int {
        return alimentos.size
    }

    override fun onBindViewHolder(holder: AlimentosViewHolder, position: Int) {
        val item = alimentos[position]
        val item2 = colacion[position]
        holder.render(item,item2,ruta,onClickListener, onClickListenerButon)
    }
}