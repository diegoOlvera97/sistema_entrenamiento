package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class Calorias(
    var idCalorias:Int,
    var idUser: Int,
    var fechaDatos: Date,
    var calorias: Double
)
