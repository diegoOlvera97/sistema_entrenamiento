package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import org.json.JSONObject
import kotlin.math.abs

class Fondos (private val datosEjercicio: DatosEjercicioActual): TipoDeEjercicio {

        val ayudaTrigonometrica = AyudaTrigonometrica()
        val imageResources = listOf(
            R.drawable.fondos
        )
        val probabilidad = 0.8
        val error = 0.40
        override fun analyzeDataEjercicio(
            landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>,
            jsonString: String
        ): DatosEjercicioActual {

            // Convertir la cadena JSON en un objeto JSONObject
            val jsonObject = JSONObject(jsonString)
            // Acceder al arreglo "angles" dentro del objeto JSON
            val anglesArray = jsonObject.getJSONArray("angles")
            // Obtener las dimensiones del arreglo bidimensional
            val numRows = anglesArray.length()
            val numCols = anglesArray.getJSONArray(0).length()
            // Declarar el arreglo bidimensional
            val angles = Array(numRows) { DoubleArray(numCols) }
            // Llenar el arreglo bidimensional con los valores del JSON
            for (i in 0 until numRows) {
                val angleSet = anglesArray.getJSONArray(i)
                for (j in 0 until numCols) {
                    angles[i][j] = angleSet.getDouble(j)
                }
            }
            val angleA = ayudaTrigonometrica.findAngle(16,14,12,landmarksGraficos)
            val angleB = ayudaTrigonometrica.findAngle(15,13,11,landmarksGraficos)
            val angleC = ayudaTrigonometrica.findAngle(12,24,26,landmarksGraficos)
            val angleD = ayudaTrigonometrica.findAngle(11,23,25,landmarksGraficos)
            val angleE = ayudaTrigonometrica.findAngle(24,26,28,landmarksGraficos)
            val angleF = ayudaTrigonometrica.findAngle(23,25,27,landmarksGraficos)
            val angleG = ayudaTrigonometrica.findAngle(14, 12, 24, landmarksGraficos)
            val angleH = ayudaTrigonometrica.findAngle(13, 11, 23, landmarksGraficos)
            val angleI = ayudaTrigonometrica.findAngle(28, 24, 23, landmarksGraficos)
            val angleJ = ayudaTrigonometrica.findAngle(24, 23, 27, landmarksGraficos)

            datosEjercicio.porcentajeSucces = (( abs((angleA-angles[datosEjercicio.form][0])/(angles[datosEjercicio.form][0]))+
                    abs((angleB-angles[datosEjercicio.form][1])/(angles[datosEjercicio.form][1]))+
                    abs((angleC-angles[datosEjercicio.form][2])/(angles[datosEjercicio.form][2]))+
                    abs((angleD-angles[datosEjercicio.form][3])/(angles[datosEjercicio.form][3]))+
                    abs((angleE-angles[datosEjercicio.form][4])/(angles[datosEjercicio.form][4])))/5)*100

            if(  confirmAngle(angleA,angles[datosEjercicio.form][0]) > probabilidad &&
                confirmAngle(angleB,(angles[datosEjercicio.form][1])) > probabilidad &&
                confirmAngle(angleC,angles[datosEjercicio.form][2]) > probabilidad &&
                confirmAngle(angleD,angles[datosEjercicio.form][3]) > probabilidad &&
                confirmAngle(angleE,angles[datosEjercicio.form][4]) > probabilidad &&
                confirmAngle(angleF,angles[datosEjercicio.form][5]) > probabilidad
            ){

                /*
                    Escribir la indicación deseada en una determinada posicion(utilizar programa de python)
                 */
                datosEjercicio.feeback = when(datosEjercicio.form){
                    0 -> "Abajo"
                    6 -> "Arriba"
                    else -> ""
                }

                datosEjercicio.form+=1
                if (datosEjercicio.form >= angles.size){
                    datosEjercicio.count+=1
                    datosEjercicio.form = 0
                }
            }
            //Metodo que compara la similitud de la postura


            return DatosEjercicioActual(datosEjercicio.count,
                datosEjercicio.porcentajeSucces,
                datosEjercicio.direction,
                datosEjercicio.form,
                datosEjercicio.feeback)
        }

        fun confirmAngle(angle:Double, trueAngle:Double):Double{
            var relacionAngulos = angle/trueAngle
            if (relacionAngulos > 1.10 + 1-probabilidad ){
                relacionAngulos = 0.0
            }
            return relacionAngulos
        }
        override fun getListImage(): List<Int> {
            return imageResources
        }

    override fun getPoseCSV():List<String> {
        return listOf("pose/fitness_poses_fondos.csv", "fondos_down")
    }

    override fun getNameEjercicio(): String {
        return "Fondos"
    }

}

/*
            abs(angleA-angles[datosEjercicio.form][0])/(angles[datosEjercicio.form][0]) < error &&
            abs(angleB-angles[datosEjercicio.form][1])/(angles[datosEjercicio.form][1]) < error &&
            abs(angleC-angles[datosEjercicio.form][2])/(angles[datosEjercicio.form][2]) < error &&
            abs(angleD-angles[datosEjercicio.form][3])/(angles[datosEjercicio.form][3]) < error &&
            abs(angleF-angles[datosEjercicio.form][4])/(angles[datosEjercicio.form][4]) < error &&
            abs(angleG-angles[datosEjercicio.form][5])/(angles[datosEjercicio.form][5]) < error


            angleA/(angles[datosEjercicio.form][0]) > probabilidad &&
            angleB/(angles[datosEjercicio.form][1]) > probabilidad &&
            angleC/(angles[datosEjercicio.form][2]) > probabilidad &&
            angleD/(angles[datosEjercicio.form][3]) > probabilidad &&
            angleE/(angles[datosEjercicio.form][4]) > probabilidad &&
            angleF/(angles[datosEjercicio.form][5]) > probabilidad
     */