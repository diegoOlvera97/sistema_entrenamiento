package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.Serializable

@Serializable
data class TiempoEjercicios(
    val nombre:String,
    val tiempo:Double
)
