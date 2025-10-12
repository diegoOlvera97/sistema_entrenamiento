package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces

import android.content.Context
import android.media.MediaPlayer
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeVoz

class VozHombre(context: Context):TipoDeVoz{
    val context = context
    override fun reproduceIndicacion(indicacion: String):Int{
        var idSound = when(indicacion){
            "Arriba" -> context.getSoundResourceId("arriba_h")
            "Abajo" -> context.getSoundResourceId("abajo_h")
            "Corrige" -> context.getSoundResourceId("corrige_h")
            "Izquierda" -> context.getSoundResourceId("izquierda_h")
            "Derecha" -> context.getSoundResourceId("derecha_h")
            "Regresa" -> context.getSoundResourceId("regresa_h")
            else -> throw IllegalArgumentException("Esa indicacion no existe")
        }
        return idSound
    }
    fun Context.getSoundResourceId(soundFileName: String): Int {
        return  context.resources.getIdentifier(soundFileName,"raw",context.packageName)
    }
}