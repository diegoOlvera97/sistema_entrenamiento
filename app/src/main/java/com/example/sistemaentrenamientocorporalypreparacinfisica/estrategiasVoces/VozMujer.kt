package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces

import android.content.Context
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeVoz

class VozMujer(context: Context): TipoDeVoz {

    val context = context
    override fun reproduceIndicacion(indicacion: String): Int {

        var idSound = when(indicacion){
            "Arriba" -> context.getSoundResourceId("arriba_m")
            "Abajo" -> context.getSoundResourceId("abajo_m")
            "Corrige" -> context.getSoundResourceId("corrige_m")
            "Izquierda" -> context.getSoundResourceId("izquierda_m")
            "Derecha" -> context.getSoundResourceId("derecha_m")
            "Regresa" -> context.getSoundResourceId("regresa_m")
            else -> throw IllegalArgumentException("Esa indicacion no existe")
        }
        return idSound
    }

    fun Context.getSoundResourceId(soundFileName: String): Int {
        return  context.resources.getIdentifier(soundFileName,"raw",context.packageName)
    }

}