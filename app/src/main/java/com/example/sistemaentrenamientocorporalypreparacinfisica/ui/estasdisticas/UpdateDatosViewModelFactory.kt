package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpdateDatosViewModelFactory(
    private val idUser: String,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateDatosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpdateDatosViewModel(idUser, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
