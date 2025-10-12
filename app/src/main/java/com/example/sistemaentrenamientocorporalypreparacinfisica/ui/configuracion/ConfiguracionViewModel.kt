package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.configuracion

import android.util.Log
import androidx.lifecycle.*
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import com.example.sistemaentrenamientocorporalypreparacinfisica.User1


class ConfiguracionViewModel(
    private val idUser: String
) : ViewModel() {

    private val _userData = MutableLiveData<User1>()
    val userData: LiveData<User1> = _userData

    private val TAG = "ConfiguracionViewModel"

    init {
        consultaDatosUser()
    }

    private fun consultaDatosUser() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("user")
                    .select(Columns.ALL) {      // selecciona todas las columnas
                        filter { eq("idUser", idUser) }
                    }
                    .decodeSingle<User1>()   // o UserData si prefieres


                _userData.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener datos de usuario: ${e.message}")
            }
        }
    }
}

class ConfiguracionViewModelFactory(
    private val idUser: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfiguracionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfiguracionViewModel(idUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
