package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.Serializable


// ObjetivosPython actualizado para reflejar la nueva vista basada en partetrabajocuerpo
@Serializable
data class ObjetivosPython(
    val idUser: Int,
    val nombreUsuario: String,
    val idObjetivo: Int,        // ahora corresponde a idParteTrabajo
    val nombreObjetivo: String, // ahora corresponde a parteCuerpo
    val tieneRelacion: Float    // 1.0 si el usuario tiene asignado el objetivo, 0.0 si no
)