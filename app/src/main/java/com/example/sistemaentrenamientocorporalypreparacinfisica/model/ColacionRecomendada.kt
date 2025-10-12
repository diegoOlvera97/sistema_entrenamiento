package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class ColacionRecomendada(
    val idColacionRecomendada: Int = 0,
    val idColacion: Int,
    val idUser: Int,
    val fechaConsumo: Date,
    val consumida: Boolean
)
