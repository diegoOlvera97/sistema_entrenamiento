package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario.objetivos

import android.content.Context
import android.provider.Telephony.Mms.Part
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioObjetivosInsert
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class CuestionarioObjetivosViewModel(
    private val context: Context
) : ViewModel() {

    private val _objetivos = MutableLiveData<List<ParteTrabajoCuerpo>>()
    val objetivos: LiveData<List<ParteTrabajoCuerpo>> = _objetivos

    private val TAG = "CuestionarioObjetivosVM"

    init {
        getObjetivos()
    }

    private fun getObjetivos() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("partetrabajocuerpo")
                    .select()
                    .decodeList<ParteTrabajoCuerpo>() // ✅ usamos el modelo correcto
                _objetivos.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener objetivos: ${e.message}")
                _objetivos.value = emptyList()
            }
        }
    }

    fun insertarObjetivoUsuario(
        idUser: Int,
        idParteTrabajo: Int,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                SupabaseInstance.client.postgrest
                    .from("objetivosusuario")
                    .insert(UsuarioObjetivosInsert(idUser, idParteTrabajo))
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error insertando objetivo: ${e.message}")
                onComplete(false)
            }
        }
    }
}
