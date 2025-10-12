package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class JumpingJack(private val datosEjercicio: DatosEjercicioActual): TipoDeEjercicio {
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
        return listOf("pose/fitness_poses_tijera_out.csv","tijera_down")
    }

    override fun getNameEjercicio(): String {
        return "Jumping Jack"
    }

    override fun analyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,json:String): DatosEjercicioActual {

        val Left_wrist = ayudaTrigonometrica.findAngle(11,15,31,landmarksGraficos)
        val Right_Wrist = ayudaTrigonometrica.findAngle(12,16,32,landmarksGraficos)
        val Left_foot_index = ayudaTrigonometrica.findAngle(24,26,28,landmarksGraficos)
        val Right_foot_index = ayudaTrigonometrica.findAngle(23,25,27,landmarksGraficos)

        // Porcentaje de éxito del PushUp!
        var per = ayudaTrigonometrica.interp(Left_wrist + Right_Wrist, Pair(50.0, 170.0), Pair(0.0,100.0))

        datosEjercicio.porcentajeSucces = per
        // Verifique el rango completo de movimiento para la flexión

        if (Left_foot_index > 150 && Right_foot_index > 150){
            datosEjercicio.form = 1
        }

        if(datosEjercicio.form == 1){
            if(per == 0.0){
                if(Left_wrist < 100 && Right_Wrist < 180){
                    datosEjercicio.feeback = "Arriba"
                    if (datosEjercicio.direction == 0.0){
                        datosEjercicio.count += 0.3
                        datosEjercicio.direction = 1.0
                    }
                }else{
                    datosEjercicio.feeback = "Corrige"
                }
            }
            if(per ==100.0){
                if(Left_foot_index > 170 && Right_foot_index > 170){
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