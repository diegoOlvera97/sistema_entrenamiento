package com.example.sistemaentrenamientocorporalypreparacinfisica.model

data class ColacionModel(
    val idColacion: Int,
    val nombre: String,
    val ingredientes:String,
    val descripcion: String,
    val imagen: String,
    val calorias: String?,
    val tipoComida:String
)
