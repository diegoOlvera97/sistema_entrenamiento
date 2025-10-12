/*package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.loginregistro

import android.util.Log
import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.FirebaseTooManyRequestsException

class AuthRepository(private val firebaseAuth: FirebaseAuth) {
    fun login(email: String, password: String): Task<AuthResult> {
        // Validar entradas
        if (email.isBlank() || password.isBlank()) {
            Log.e("AuthRepository", "Email o contraseña vacíos")
            return com.google.android.gms.tasks.Tasks.forException(
                IllegalArgumentException("Email o contraseña no pueden estar vacíos")
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e("AuthRepository", "Formato de email inválido: $email")
            return com.google.android.gms.tasks.Tasks.forException(
                IllegalArgumentException("Formato de email inválido")
            )
        }

        // Log para depurar entradas
        Log.d("DEBUG AuthRepository", "Intentando login con email: $email, password: [HIDDEN]")

        return firebaseAuth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    Log.d("DEBUG AuthRepository", "Login exitoso para el usuario: ${user?.email}, UID: ${user?.uid}")
                } else {
                    val exception = task.exception
                    Log.e("LoginError", "Error de credenciales: ${exception?.message}")
                    when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Log.e("AuthRepository", "Credenciales incorrectas")
                        }
                        is FirebaseAuthInvalidUserException -> {
                            Log.e("AuthRepository", "El usuario no existe o ha sido deshabilitado")
                        }
                        is FirebaseTooManyRequestsException -> {
                            Log.e("AuthRepository", "Demasiados intentos, intenta de nuevo más tarde")
                        }
                        else -> {
                            Log.e("AuthRepository", "Error desconocido: ${exception?.message}")
                        }
                    }
                }
            }
    }

    fun registro(email: String, password: String): Task<AuthResult> {
        // Validar entradas
        if (email.isBlank() || password.isBlank()) {
            Log.e("AuthRepository", "Email o contraseña vacíos")
            return com.google.android.gms.tasks.Tasks.forException(
                IllegalArgumentException("Email o contraseña no pueden estar vacíos")
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e("AuthRepository", "Formato de email inválido: $email")
            return com.google.android.gms.tasks.Tasks.forException(
                IllegalArgumentException("Formato de email inválido")
            )
        }
        if (password.length < 6) {
            Log.e("AuthRepository", "La contraseña debe tener al menos 6 caracteres")
            return com.google.android.gms.tasks.Tasks.forException(
                IllegalArgumentException("La contraseña es demasiado corta")
            )
        }

        // Log para depurar entradas
        Log.d("DEBUG AuthRepository", "Intentando registro con email: $email, password: [HIDDEN]")

        return firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnSuccessListener {
                Log.d("AuthRepository", "Registro exitoso para el usuario: $email, UID: ${it.user?.uid}")
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthWeakPasswordException -> {
                        Log.e("AuthRepository", "La contraseña es demasiado débil")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Log.e("AuthRepository", "Formato de email inválido")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        Log.e("AuthRepository", "El email ya está registrado")
                    }
                    is FirebaseTooManyRequestsException -> {
                        Log.e("AuthRepository", "Demasiados intentos, intenta de nuevo más tarde")
                    }
                    else -> {
                        Log.e("AuthRepository", "Error en el registro: ${exception.message}", exception)
                    }
                }
            }
    }
}*/