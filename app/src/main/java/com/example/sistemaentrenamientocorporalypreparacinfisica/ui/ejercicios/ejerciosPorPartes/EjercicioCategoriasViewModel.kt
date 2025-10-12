package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class EjercicioCategoriasViewModel(
    private val context: Context
) : ViewModel() {

    private val _listaCategorias = MutableLiveData<List<ParteTrabajoCuerpo>>()
    val listaCategorias: LiveData<List<ParteTrabajoCuerpo>> = _listaCategorias

    private val TAG = "EjerciciosCategoriasVM"

    init {
        consultaListaEjercicioPartes()
    }

    private fun consultaListaEjercicioPartes() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("partetrabajocuerpo")
                    .select(Columns.ALL)
                    .decodeList<ParteTrabajoCuerpo>()

                _listaCategorias.value = response

            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener partes del cuerpo desde Supabase: ${e.message}", e)
            }
        }
    }
}
