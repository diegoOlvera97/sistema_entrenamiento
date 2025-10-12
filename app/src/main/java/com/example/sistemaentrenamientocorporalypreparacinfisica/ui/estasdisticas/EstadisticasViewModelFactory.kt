package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jan.supabase.postgrest.*

class EstadisticasViewModelFactory(
    private val idUser: String,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstadisticasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstadisticasViewModel(idUser, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}