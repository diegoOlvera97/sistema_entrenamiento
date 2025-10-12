package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsCardParteTrajadaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsCardRutinasBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo

class EjerciciosViewHolder (view:View):RecyclerView.ViewHolder(view){
    val binding = ItemsCardParteTrajadaBinding.bind(view)

    fun render(semana: ParteTrabajoCuerpo, onClickListener: (ParteTrabajoCuerpo)->Unit){
        binding.texto.text = semana.parteCuerpo
        itemView.setOnClickListener{onClickListener(semana)}

        // Agregar seis imagenes dependiendo de la rutina si es que se va a utilizar una
        // Glide.with(binding.albumimgCardLh.context).load(songModel.img_url).into(binding.albumimgCardLh) para agregar a cada imagen
        // deoendencia implementation ("com.github.bumptech.glide:glide:4.16.0")
        /*
        binding.img1.setImageResource(R.drawable.ejercicio_realizar)
        binding.img2.setImageResource(R.drawable.img1p)
        binding.img3.setImageResource(R.drawable.img2p)
        binding.img4.setImageResource(R.drawable.img3p)
        binding.img5.setImageResource(R.drawable.img4p)
        binding.img6.setImageResource(R.drawable.img5p)
         */


    }
}

