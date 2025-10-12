package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class Lunges(private val datosEjercicio: DatosEjercicioActual): TipoDeEjercicio {
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
        return listOf("pose/fitness_poses_zancada_out.csv","zancada_left")
    }

    override fun getNameEjercicio(): String {
        return "Lunges"
    }

    override fun analyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,json:String): DatosEjercicioActual {

        val Left_knee = ayudaTrigonometrica.findAngle(27,25,23,landmarksGraficos)
        val Rigth_knee = ayudaTrigonometrica.findAngle(28,26,24,landmarksGraficos)
        val Hip = ayudaTrigonometrica.findAngle(28,26,24,landmarksGraficos)

        // Porcentaje de éxito del PushUp!
        var per = ayudaTrigonometrica.interp(Left_knee, Pair(90.0, 160.0), Pair(0.0,100.0))

        datosEjercicio.porcentajeSucces = per
        // Verifique el rango completo de movimiento para la flexión

        if (Left_knee > 130 && Rigth_knee > 90 && Hip > 130){
            datosEjercicio.form = 1
        }

        if(datosEjercicio.form == 1){
            if(per == 0.0){
                if(Left_knee <= 120 && Hip > 90){
                    datosEjercicio.feeback = "Izquierda"
                    if (datosEjercicio.direction == 0.0){
                        datosEjercicio.count += 0.5
                        datosEjercicio.direction = 1.0
                    }
                }else{
                    datosEjercicio.feeback = "Derecha"
                }
            }
            if(per ==100.0){
                if(Left_knee > 120 && Rigth_knee > 120 && Hip > 90){
                    datosEjercicio.feeback = "Derecha"
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