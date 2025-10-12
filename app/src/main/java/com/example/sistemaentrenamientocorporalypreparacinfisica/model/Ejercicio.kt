package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import kotlinx.serialization.Serializable

@Serializable
data class Ejercicio(
    val idEjercicio: Int,
    val idParteTrabajo: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String
)
