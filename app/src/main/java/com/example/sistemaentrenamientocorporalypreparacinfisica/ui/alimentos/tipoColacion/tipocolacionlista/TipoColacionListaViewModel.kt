package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoColacion.tipocolacionlista

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class TipoColacionListaViewModel(
    private val tipoColacion: String,
    private val context: Context
) : ViewModel() {

    private val _listaColacion = MutableLiveData<List<Colacion>>()
    val listaColacion: LiveData<List<Colacion>> = _listaColacion

    private val TAG = "TipoColacionViewModel"

    init {
        cargarLista(tipoColacion)
    }

    private fun cargarLista(tipoColacion: String) {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("colacion")
                    .select {
                        filter {
                            eq("tipoColacion", tipoColacion)
                        }
                    }
                    .decodeList<Colacion>()

                _listaColacion.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar colaciones: ${e.message}", e)
                _listaColacion.value = emptyList()
            }
        }
    }
}
