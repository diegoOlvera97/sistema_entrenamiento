package com.example.sistemaentrenamientocorporalypreparacinfisica.model

data class Alimentos(
    val idAlimento:Int,
    val nombre: String,
    val ingredientes:String,
    val descripcion: String,
    val imagen: String,
    val calorias: Double?,
    val tipoComida:String
)
