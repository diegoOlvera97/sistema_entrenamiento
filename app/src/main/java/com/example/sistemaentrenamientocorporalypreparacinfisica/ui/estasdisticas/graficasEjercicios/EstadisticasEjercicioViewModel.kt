package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas.graficasEjercicios

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizado
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizadoJson
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizadoLista
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.TiempoEjercicios
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import java.time.LocalDate


class EstadisticasEjercicioViewModel(
    private val idUser: Int,
    private val context: Context
) : ViewModel() {

    private val _tiempoPorEjercicios = MutableLiveData<List<TiempoEjercicios>>()
    val tiempoEjercicios: LiveData<List<TiempoEjercicios>> = _tiempoPorEjercicios

    private val _datosEjerciciosRealizados = MutableLiveData<List<EjercicioRealizadoLista>>()
    val datosEjerciciosRealizados: LiveData<List<EjercicioRealizadoLista>> = _datosEjerciciosRealizados

    private val TAG = "EstadisticasEjercicioVM"

    init {
        fetchTiempoEjercicios()
        fetchEjerciciosRealizados()
    }

    private fun fetchTiempoEjercicios() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("vista_tiempo_ejercicios")
                    .select {
                        filter { eq("idUser", idUser) }
                        //order("total_tiempo", ascending = false) // orden descendente
                    }
                    .decodeList<TiempoEjercicios>()
                _tiempoPorEjercicios.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener tiempo por ejercicio: ${e.message}", e)
                _tiempoPorEjercicios.value = emptyList()
            }
        }
    }

    private fun fetchEjerciciosRealizados() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("vista_ejercicios_realizados")
                    .select {
                        filter { eq("idUser", idUser) }
                        //order("idEjercicio", ascending = true)
                        //order("fechaEjercicio", ascending = true)
                    }
                    .decodeList<EjercicioRealizadoLista>()
                _datosEjerciciosRealizados.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener ejercicios realizados: ${e.message}", e)
                _datosEjerciciosRealizados.value = emptyList()
            }
        }
    }



}
