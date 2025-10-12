package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.Calorias
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvances
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.*
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.launch

class EstadisticasViewModel(
    private val idUser: String,
    private val context: Context
) : ViewModel() {

    private val _usuarioAvances = MutableLiveData<List<UsuarioAvances>>()

    private val _usuarioAvancesData = MutableLiveData<UsuarioAvances>()
    val usuarioAvancesData: LiveData<UsuarioAvances> = _usuarioAvancesData

    private val _caloriasLista = MutableLiveData<List<Calorias>>()
    val caloriasLista: LiveData<List<Calorias>> = _caloriasLista

    private val TAG = "EstadisticasViewModel"

    init {
        avancesDelUsuario()
        avanceMasActual()
        listaCalorias()
    }

    // 🔹 Avance más reciente del usuario
    private fun avancesDelUsuario() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("usuarioavances")
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser) }
                    }
                    .decodeList<UsuarioAvances>()

                _usuarioAvances.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error en avancesDelUsuario: ${e.message}")
                _usuarioAvances.value = emptyList()
            }
        }
    }

    private fun avanceMasActual() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("usuarioavances")
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser) }
                        order("fechaDatos",Order.DESCENDING)
                    }

                    .decodeList<UsuarioAvances>()

                _usuarioAvancesData.value = response.firstOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error en avanceMasActual: ${e.message}", e)
            }
        }
    }


    /**
     * ✅ Obtener lista de calorías del usuario
     */
    private fun listaCalorias() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("calorias")
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser) }
                    }
                    .decodeList<Calorias>()

                _caloriasLista.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error en listaCalorias: ${e.message}")
                _caloriasLista.value = emptyList()
            }
        }
    }


    // 🔹 Refrescar datos manualmente
    fun updateData() {
        avancesDelUsuario()
        avanceMasActual()
        listaCalorias()
    }
}
