package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ObjetivosPython
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.RepeticionesPython
import io.github.jan.supabase.postgrest.postgrest

class EjercicioActividadViewModel(
    private val idUser: String,
    private val context: Context
) : ViewModel() {

    private val _objetivos = MutableLiveData<List<ObjetivosPython>>()
    val objetivosUsuarios: LiveData<List<ObjetivosPython>> = _objetivos

    private val _repeticiones = MutableLiveData<List<RepeticionesPython>>()
    val repeticionesUsuarios: LiveData<List<RepeticionesPython>> = _repeticiones

    private val _listejercicios = MutableLiveData<List<Ejercicio>>()
    val listaejercicios: LiveData<List<Ejercicio>> = _listejercicios

    private val _datosReps = MutableLiveData<List<Float>>()
    val datosReps: LiveData<List<Float>> = _datosReps

    private val TAG = "RutinaViewModel"

    init {
        fetchObjetivos()
        fetchRepeticiones()
        fetchEjercicios()
    }

    private fun fetchObjetivos() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("vista_objetivos_usuario")
                    .select {
                        filter { eq("idUser", idUser) }
                    }
                    .decodeList<ObjetivosPython>()
                _objetivos.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener objetivos: ${e.message}", e)
                _objetivos.value = emptyList()
            }
        }
    }

    private fun fetchRepeticiones() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("vista_repeticiones_usuario")
                    .select {
                        filter { eq("idUser", idUser) }
                    }
                    .decodeList<RepeticionesPython>()
                _repeticiones.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener repeticiones: ${e.message}", e)
                _repeticiones.value = emptyList()
            }
        }
    }

    private fun fetchEjercicios() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("ejercicio")
                    .select()
                    .decodeList<Ejercicio>()
                _listejercicios.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener ejercicios: ${e.message}", e)
                _listejercicios.value = emptyList()
            }
        }
    }

    fun setdataReps(list: List<Float>) {
        _datosReps.value = list
    }
}
