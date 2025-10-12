package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.recomendaciones

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

class ColacionViewModel(
    private val idColacion: String,
    private val context: Context
) : ViewModel() {

    private val _colacion = MutableLiveData<Colacion>()
    val colacion: LiveData<Colacion> = _colacion

    private val TAG = "ColacionViewModel"

    init {
        consultaColacion()
    }

    private fun consultaColacion() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("colacion")
                    .select {
                        filter {
                            eq("idColacion", idColacion.toInt())
                        }
                    }
                    .decodeList<Colacion>()

                _colacion.value = response.firstOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar colación: ${e.message}", e)
                _colacion.value = null
            }
        }
    }
}
