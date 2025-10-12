package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.recomendaciones

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

import kotlinx.coroutines.launch

class AlimentoViewModel(
    private val idAlimento: String,
    private val context: Context
) : ViewModel() {

    private val _alimento = MutableLiveData<Alimentos>()
    val alimento: LiveData<Alimentos> = _alimento

    private val TAG = "AlimentosViewModel"

    init {
        consultaAlimento()
    }

    private fun consultaAlimento() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("alimentos")
                    .select (Columns.ALL){
                        filter {
                            eq("idAlimento", idAlimento.toLong()) // 👈 bigint, convertimos String a Long
                        }
                    }
                    .decodeList<Alimentos>()

                _alimento.value = response.firstOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar alimento: ${e.message}", e)
            }
        }
    }
}
