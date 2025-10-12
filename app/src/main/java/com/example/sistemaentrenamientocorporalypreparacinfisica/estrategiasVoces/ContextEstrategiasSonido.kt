package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces

import android.content.Context
import android.media.MediaPlayer
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeVoz

class ContextEstrategiasSonido(private var tipoDeVoz: TipoDeVoz){
    fun excuteSonido(indicacion:String):Int{
        return tipoDeVoz.reproduceIndicacion(indicacion)
    }
}