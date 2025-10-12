package com.example.sistemaentrenamientocorporalypreparacinfisica.model

import java.util.Date

data class UserData(
    val idUser: Int,
    val correo: String,
    val metodoLogue: String,
    val loginSucces: Boolean,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: Date,
    val telefono: String,
    val sexo: Char,
    val experenciaPrevia: Char?
)
