package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.User1
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvances
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val context: Context,
    private val idUser: String
) : ViewModel() {

    private val TAG = "NotificationsViewModel"

    private val _usuarioAvancesData = MutableLiveData<UsuarioAvances>()
    val usuarioAvancesData: LiveData<UsuarioAvances> = _usuarioAvancesData

    private val _userData = MutableLiveData<User1>()
    val userData: LiveData<User1> = _userData

    init {
        cargarDatosUsuario()
        cargarAvanceMasActual()
    }

    private fun cargarDatosUsuario() {
        viewModelScope.launch {
            try {
                val user = SupabaseInstance.client.postgrest
                    .from("user") // 👈 nombre de tu tabla en Supabase
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser.toInt()) }
                        limit(1)
                    }
                    .decodeSingle<User1>()

                _userData.postValue(user)

            } catch (e: Exception) {
                Log.e(TAG, "Error cargando datos de usuario: ${e.message}", e)
            }
        }
    }

    private fun cargarAvanceMasActual() {
        viewModelScope.launch {
            try {
                val avance = SupabaseInstance.client.postgrest
                    .from("usuarioavances") // 👈 nombre de tu tabla en Supabase
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser.toInt()) }
                        order("fechaDatos", Order.DESCENDING)
                        limit(1)
                    }
                    .decodeSingle<UsuarioAvances>()

                _usuarioAvancesData.postValue(avance)

            } catch (e: Exception) {
                Log.e(TAG, "Error cargando avances: ${e.message}", e)
            }
        }
    }
}
