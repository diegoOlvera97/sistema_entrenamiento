package com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces

import android.content.Context
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual

interface TipoDeEjercicio{
    fun analyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>>,jsonString:String):DatosEjercicioActual
    fun getListImage():List<Int>
    fun getPoseCSV():List<String>
    fun getNameEjercicio():String
}