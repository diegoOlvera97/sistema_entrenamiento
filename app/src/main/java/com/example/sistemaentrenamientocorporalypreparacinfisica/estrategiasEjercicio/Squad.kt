package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class Squad (private val data: DatosEjercicioActual): TipoDeEjercicio {
    private val ayudaTrigonometrica = AyudaTrigonometrica()
    val imageResources = listOf(
        R.drawable.mesh_gradient,
        R.drawable.fitness_center_24px,
        R.drawable.comida
    )
    override fun getListImage(): List<Int> {
        return imageResources
    }

    override fun getPoseCSV(): List<String> {
        return listOf("pose/fitness_pose_suqad_out.csv", "squats_down")
    }

    override fun getNameEjercicio(): String {
        return "Squad"
    }

    override fun analyzeDataEjercicio(
        landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,
        jsonString: String
    ): DatosEjercicioActual {

        val Left_knee = ayudaTrigonometrica.findAngle( 28, 26, 24,landmarksGraficos)
        val Left_shoulder = ayudaTrigonometrica.findAngle(12, 24, 26,landmarksGraficos)
        val Left_hip = ayudaTrigonometrica.findAngle(26, 28, 30,landmarksGraficos)

        // Porcentaje de éxito del PushUp!
        var per = ayudaTrigonometrica.interp(Left_knee, Pair(145.0, 160.0), Pair(0.0,100.0))
        data.porcentajeSucces = per
        //Revisar la forma correcta antes de comenzar el programa.
        if (Left_knee > 160 && Left_shoulder > 90 && Left_hip > 160){
            data.form = 1
        }
        if (data.form == 1){
            if (data.porcentajeSucces == 0.0){
                if(Left_knee <= 90 && Left_hip > 85){
                    //data.feeback = "Arriba"
                    if (data.direction == 0.0){
                        data.count += 0.5
                        data.direction = 1.0
                    }
                }
            }else{
                //data.feeback = "Corrige"
            }
            if (data.porcentajeSucces == 100.0){
                if(Left_knee > 160 && Left_shoulder > 90 && Left_hip > 85){
                    //data.feeback = "Abajo"
                    if (data.direction == 1.0){
                        data.count += 0.5
                        data.direction = 0.0
                    }
                }
            }else{
                //data.feeback = "Corrige"
            }
        }

        return DatosEjercicioActual(data.count,
            data.porcentajeSucces,
            data.direction,
            data.form,
            data.feeback)
    }
}