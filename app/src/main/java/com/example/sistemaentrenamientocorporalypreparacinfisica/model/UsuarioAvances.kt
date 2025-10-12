package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class UsuarioAvances(
    var idUserAvances:Int,
    var idUser: Int,
    var fechaDatos: Date,
    var peso: Double,
    var altura: Double,
    var pesoGrasa: Double,
    var pesoMusculo: Double
)
