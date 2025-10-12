package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.Serializable


// RepeticionesPython se mantiene igual, ya que no hay cambios en la tabla de ejercicios
@Serializable
data class RepeticionesPython(
    val idUser: Int,
    val nombreUsuario: String,
    val idEjercicio: Int,
    val nombreEjercicio: String,
    val totalRepeticiones: Float
)