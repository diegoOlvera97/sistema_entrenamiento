package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class AlimentoRecomendado(
    val idComidaRecomendada: Int = 0,
    val idAlimento: Int,
    val idUser: Int,
    val fechaConsumo: Date,
    val consumida: Boolean
)
