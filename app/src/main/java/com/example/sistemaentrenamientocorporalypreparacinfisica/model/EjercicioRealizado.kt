package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class EjercicioRealizado(
    val fechaEjercicio: String,
    val repeticiones: Int,
    val tiempo: Double,
)

