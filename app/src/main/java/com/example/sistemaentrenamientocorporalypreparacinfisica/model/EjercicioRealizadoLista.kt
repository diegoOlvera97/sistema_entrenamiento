package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EjercicioRealizadoLista(
    val nombre: String,
    @SerialName("ejerciciorealizado")val ejercicioRealizado: List<EjercicioRealizado>? = emptyList()
)
