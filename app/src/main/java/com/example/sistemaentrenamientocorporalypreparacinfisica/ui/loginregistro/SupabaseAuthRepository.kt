package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.loginregistro

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.github.jan.supabase.gotrue.auth

class SupabaseAuthRepository(private val supabase: SupabaseClient) {

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            Log.e("AuthRepository", "Email o contraseña vacíos")
            callback(false, "Email o contraseña no pueden estar vacíos")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email.trim()
                    this.password = password.trim()
                }

                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    Log.d("AuthRepository", "Login exitoso para: ${user.email}, UID: ${user.id}")
                    callback(true, null)
                } else {
                    Log.d("AuthRepository", "Login completado, verificar email si es necesario")
                    callback(false, "Login completado, verificar email si es necesario")
                }

            } catch (e: Exception) {
                Log.e("AuthRepository", "Error en login: ${e.message}", e)
                callback(false, e.message)
            }
        }
    }

    fun registro(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            Log.e("AuthRepository", "Email o contraseña vacíos")
            callback(false, "Email o contraseña no pueden estar vacíos")
            return
        }

        if (password.length < 6) {
            Log.e("AuthRepository", "La contraseña es demasiado corta")
            callback(false, "La contraseña debe tener al menos 6 caracteres")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email.trim()
                    this.password = password.trim()
                }

                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    Log.d("AuthRepository", "Registro exitoso: ${user.email}, UID: ${user.id}")
                    callback(true, null)
                } else {
                    Log.d("AuthRepository", "Registro completado, verificar email si es necesario")
                    callback(false, "Registro completado, verificar email si es necesario")
                }

            } catch (e: Exception) {
                Log.e("AuthRepository", "Error en registro: ${e.message}", e)
                callback(false, e.message)
            }
        }
    }
}
