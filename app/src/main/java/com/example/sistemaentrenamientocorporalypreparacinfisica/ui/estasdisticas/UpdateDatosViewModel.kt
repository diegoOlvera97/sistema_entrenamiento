package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvances
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class UpdateDatosViewModel(
    private val idUser: String,
    private val context: Context
) : ViewModel() {

    private val _usuarioAvances = MutableLiveData<UsuarioAvances?>()
    val usuarioAvances: LiveData<UsuarioAvances?> = _usuarioAvances

    // LiveData para notificar actualizaciones a otros ViewModels
    private val _datosActualizados = MutableLiveData<Boolean>()
    val datosActualizados: LiveData<Boolean> = _datosActualizados

    private val TAG = "UpdateDatosViewModel"

    init {
        getDataUsuarioAvances()
    }

    private fun getDataUsuarioAvances() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("usuarioavances")
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser) }
                        order("fechaDatos", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    }
                    .decodeList<UsuarioAvances>()

                _usuarioAvances.value = response.firstOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener último registro: ${e.message}", e)
                _usuarioAvances.value = null
            }
        }
    }

    fun insertDataUsuariosAvances(
        idUsuarioAvances: Int,
        peso: Double,
        altura: Double,
        pesoGrasa: Double?,
        pesoMusculo: Double?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                SupabaseInstance.client.postgrest
                    .from("usuarioavances")
                    .update(
                        mapOf(
                            "peso" to peso,
                            "altura" to altura,
                            "pesoGrasa" to pesoGrasa,
                            "pesoMusculo" to pesoMusculo
                        )
                    ) {
                        filter { eq("idUserAvances", idUsuarioAvances) }
                    }

                // 🚀 Notificamos que hubo un update
                _datosActualizados.value = true
                onResult(true)

            } catch (e: Exception) {
                Log.e(TAG, "Error al actualizar datos: ${e.message}", e)
                onResult(false)
            }
        }
    }
}
