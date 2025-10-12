package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual

class ContextoEstrategias(private var tipoDeEjercicio: TipoDeEjercicio){
    fun executeanalyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>>,json:String): DatosEjercicioActual{
        return tipoDeEjercicio.analyzeDataEjercicio(landmarksGraficos,json)
    }
    fun executegetListImage():List<Int>{
        return tipoDeEjercicio.getListImage()
    }
    fun getPoseCSV():List<String>{
        return tipoDeEjercicio.getPoseCSV()
    }
    fun getNombreEjercicio():String{
        return tipoDeEjercicio.getNameEjercicio()
    }
}