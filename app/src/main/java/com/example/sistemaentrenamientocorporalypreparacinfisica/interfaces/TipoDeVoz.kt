package com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces

import android.content.Context
import android.media.MediaPlayer

interface TipoDeVoz {
    fun reproduceIndicacion(indicacion:String):Int
}