package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class TodosLosEjerciciosViewModel(
    private val context: Context
) : ViewModel() {

    private val _listaejercicios = MutableLiveData<List<Ejercicio>>()
    val listaejercicios: LiveData<List<Ejercicio>> = _listaejercicios

    private val TAG = "TodosLosEjerciciosVM"

    init {
        consultaGetEjercicios()
    }

    private fun consultaGetEjercicios() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("ejercicio")
                    .select(Columns.ALL)
                    .decodeList<Ejercicio>()

                _listaejercicios.value = response

            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener ejercicios desde Supabase: ${e.message}", e)
            }
        }
    }
}
