package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas.graficasEjercicios

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EstadisticasEjerciciosViewModelFactory (
    private val idUser: Int,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstadisticasEjercicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstadisticasEjercicioViewModel(idUser, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}