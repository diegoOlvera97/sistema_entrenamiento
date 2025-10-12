package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class EjercicioRealizadoJson(
    val nombre:String,
    val fechaEjercicio: String,
    val repeticiones: Int,
    val tiempo: Double
)
