package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.launch

class LivePreviewActivityViewModel(
    private val context: Context
) : ViewModel() {
    private val TAG = "LivePreviewActivityViewModel"

    fun procedimiento_datos_usuario_ejercicio_realizado(
        idEjercicio: String,
        idUser: String,
        repeticiones: String?,
        tiempo: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val repsInt = repeticiones?.toIntOrNull() ?: 0
                val tiempoInt = tiempo.toIntOrNull() ?: 0

                Log.d(TAG, "📤 Llamando RPC con parámetros: idEjercicio=$idEjercicio, idUser=$idUser, repeticiones=$repsInt, tiempo=$tiempoInt")

                SupabaseInstance.client.postgrest
                    .rpc(
                        "ejercicio_realizado_usuario",
                        mapOf(
                            "idejerciciop" to idEjercicio.toInt(),
                            "iduserp" to idUser.toInt(),
                            "repeticionesp" to repsInt,
                            "tiempop" to tiempoInt
                        )
                    )

                Log.d(TAG, "✅ Procedimiento ejecutado correctamente")
                onResult(true)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al ejecutar procedimiento: ${e.message}", e)
                e.printStackTrace()
                onResult(false)
            }
        }
    }



}