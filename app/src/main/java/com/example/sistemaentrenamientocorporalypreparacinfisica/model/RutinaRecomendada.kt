package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class RutinaRecomendada(
    val idRutina: Int,
    val idUser: Int,
    val idEjercicio: Int,
    val fechaEjercicio: Date,
    val repeticiones: Int?
)
