package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import org.json.JSONObject
import kotlin.math.abs

class Burpees(private val datosEjercicio: DatosEjercicioActual): TipoDeEjercicio {
    private val ayudaTrigonometrica = AyudaTrigonometrica()
    val imageResources = listOf(
        R.drawable.burpee
    )
    override fun getListImage(): List<Int> {
        return imageResources
    }

    override fun getPoseCSV(): List<String> {
        return listOf("pose/fitness_poses_bp_out.csv","bp_down")
    }

    override fun getNameEjercicio(): String {
        return "Burpees"
    }

    override fun analyzeDataEjercicio(landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,json:String): DatosEjercicioActual {

        val Left_eye_outer = ayudaTrigonometrica.findAngle(3,19,25,landmarksGraficos)
        val Right_eye_outer = ayudaTrigonometrica.findAngle(6,20,26,landmarksGraficos)
        val Left_heel = ayudaTrigonometrica.findAngle(24,26,30,landmarksGraficos)
        val Right_heel = ayudaTrigonometrica.findAngle(23,25,29,landmarksGraficos)

        // Porcentaje de éxito del PushUp!s
        var per = ayudaTrigonometrica.interp(Left_eye_outer + Right_eye_outer, Pair(50.0, 170.0), Pair(0.0,100.0))

        datosEjercicio.porcentajeSucces = per
        // Verifique el rango completo de movimiento para la flexión

        if (Left_heel > 150 && Right_heel > 150){
            datosEjercicio.form = 1
        }

        if(datosEjercicio.form == 1){
            if(per == 0.0){
                if(Left_eye_outer < 170 && Right_eye_outer < 180){
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
                if(Left_heel > 150 && Right_heel > 150){
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