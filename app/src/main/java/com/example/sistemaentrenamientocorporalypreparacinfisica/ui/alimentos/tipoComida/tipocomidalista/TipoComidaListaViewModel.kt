package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoComida.tipocomidalista


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class TipoComidaListaViewModel(
    private val tipoComida: String
) : ViewModel() {

    private val _listaComida = MutableLiveData<List<Alimentos>>()
    val listaComida: LiveData<List<Alimentos>> = _listaComida

    private val TAG = "TipoComidaViewModel"

    init {
        cargarLista(tipoComida)
    }

    private fun cargarLista(tipoComida: String) {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("alimentos")
                    .select(Columns.ALL) {
                        filter { eq("tipoComida", tipoComida) }
                    }
                    .decodeList<Alimentos>()

                _listaComida.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar alimentos: ${e.message}", e)
                _listaComida.value = emptyList()
            }
        }
    }
}
