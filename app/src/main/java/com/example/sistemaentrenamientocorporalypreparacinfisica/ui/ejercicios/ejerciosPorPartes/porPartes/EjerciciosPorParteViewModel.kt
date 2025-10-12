package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes.porPartes

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch


class EjerciciosPorParteViewModel(
    private val idParteTrabajo: Int, // ahora es ID numérico
    private val context: Context
) : ViewModel() {

    private val _listejecicios = MutableLiveData<List<Ejercicio>>()
    val listaejercicios: LiveData<List<Ejercicio>> = _listejecicios
    private val TAG = "EjerciciosPorParteVM"

    init {
        consultaEjercicioPorParte()
    }

    private fun consultaEjercicioPorParte() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("ejercicio")
                    .select(Columns.ALL) {
                        filter { eq("idParteTrabajo", idParteTrabajo) }
                    }// FILTRO POR ID
                    .decodeList<Ejercicio>()

                _listejecicios.value = response

            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener ejercicios por parte desde Supabase: ${e.message}", e)
            }
        }
    }
}
