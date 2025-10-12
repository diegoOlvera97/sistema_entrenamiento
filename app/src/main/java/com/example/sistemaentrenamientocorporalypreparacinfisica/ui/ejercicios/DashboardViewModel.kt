package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _ejercicios = MutableLiveData<List<ParteTrabajoCuerpo>>()
    val ejercicios: LiveData<List<ParteTrabajoCuerpo>> = _ejercicios

    fun getDataEjerciciosUsuario(idUser: Int) {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("partetrabajo")
                    .select(Columns.ALL) { filter { eq("idUser", idUser) } }
                    .decodeList<ParteTrabajoCuerpo>()
                _ejercicios.value = response
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error al obtener ejercicios: ${e.message}", e)
                _ejercicios.value = emptyList()
            }
        }
    }

}