package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import android.util.Log
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class PushUp(private val datosEjercicio: DatosEjercicioActual):TipoDeEjercicio{
    private val ayudaTrigonometrica = AyudaTrigonometrica()

    val imageResources = listOf(
        R.drawable.fitness_center_24px,
        R.drawable.fitness_center_24px,
        R.drawable.fitness_center_24px
    )
    override fun getListImage(): List<Int> {
        return imageResources
    }

    override fun getPoseCSV(): List<String> {
        return listOf("pose/fitness_pose_pushups.csv","pushups_down")
    }

    override fun getNameEjercicio(): String {
        return "Push Up"
    }

    override fun analyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,json:String): DatosEjercicioActual {

        val Left_elbow = ayudaTrigonometrica.findAngle(12,14,16,landmarksGraficos)
        val Left_shoulder = ayudaTrigonometrica.findAngle(14,12,24,landmarksGraficos)
        val Left_hip = ayudaTrigonometrica.findAngle(12,24,26,landmarksGraficos)


        // Porcentaje de éxito del PushUp!
        var per = ayudaTrigonometrica.interp(Left_elbow, Pair(90.0, 160.0), Pair(0.0,100.0))

        datosEjercicio.porcentajeSucces = per
        // Verifique el rango completo de movimiento para la flexión

        if (Left_elbow > 160.0 && Left_shoulder > 40.0 && Left_hip > 160.0){
            datosEjercicio.form = 1
        }

        if(datosEjercicio.form == 1){
            if(per == 0.0){
                if(Left_elbow <= 90.0 && Left_hip > 160.0){
                    datosEjercicio.feeback = "Arriba"
                   if (datosEjercicio.direction == 0.0){
                       datosEjercicio.count += 0.5
                       datosEjercicio.direction = 1.0
                    }
                }else{
                    datosEjercicio.feeback = "Corrige"
                }
            }
            if(per ==100.0){
                if(Left_elbow > 160 && Left_shoulder > 40 && Left_hip > 160){
                    datosEjercicio.feeback = "Abajo"
                    if (datosEjercicio.direction == 1.0){
                        datosEjercicio.count +=0.5
                        datosEjercicio.direction =0.0
                    }
                }else{
                    datosEjercicio.feeback = "Corrige"
                }
            }
        }

        return DatosEjercicioActual(datosEjercicio.count,
            datosEjercicio.porcentajeSucces,
            datosEjercicio.direction,
            datosEjercicio.form,
            datosEjercicio.feeback)
    }

}