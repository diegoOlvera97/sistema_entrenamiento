package com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio

import android.content.Context
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import java.io.IOException
import java.io.InputStream
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class AyudaTrigonometrica {
    fun findAngle(a:Int, b:Int, c:Int,landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>):Double{
        if(landmarksGraficos.isNotEmpty()){
            val x1 = landmarksGraficos[0][a].x()
            val y1 = landmarksGraficos[0][a].y()
            val x2 = landmarksGraficos[0][b].x()
            val y2 = landmarksGraficos[0][b].y()
            val x3 = landmarksGraficos[0][c].x()
            val y3 = landmarksGraficos[0][c].y()
            val anguloRad = atan2(y3 - y2, x3 - x2) - atan2(y1 - y2, x1 - x2)
            var angle = Math.toDegrees(anguloRad.toDouble())

            if (angle < 0){
                angle += 360
                if (angle > 180){
                    angle = 360 - angle
                }
            }
            else if(angle > 180){
                angle = 360 - angle
            }
            return angle
        }else{
            return 0.0
        }
    }
    fun findAngleR3(a:Int, b:Int, c:Int,landmarksGraficos: MutableList<MutableList<NormalizedLandmark>>):Double{
        if (landmarksGraficos.isNotEmpty()) {
            val x1 = landmarksGraficos[0][a].x()
            val y1 = landmarksGraficos[0][a].y()
            val z1 = landmarksGraficos[0][a].z()

            val x2 = landmarksGraficos[0][b].x()
            val y2 = landmarksGraficos[0][b].y()
            val z2 = landmarksGraficos[0][b].z()

            val x3 = landmarksGraficos[0][c].x()
            val y3 = landmarksGraficos[0][c].y()
            val z3 = landmarksGraficos[0][c].z()

            val vector1 = floatArrayOf(x1 - x2, y1 - y2, z1 - z2)
            val vector2 = floatArrayOf(x3 - x2, y3 - y2, z3 - z2)

            val dotProduct = vector1[0] * vector2[0] + vector1[1] * vector2[1] + vector1[2] * vector2[2]
            val magnitude1 = sqrt(vector1[0].pow(2) + vector1[1].pow(2) + vector1[2].pow(2))
            val magnitude2 = sqrt(vector2[0].pow(2) + vector2[1].pow(2) + vector2[2].pow(2))

            val cosTheta = dotProduct / (magnitude1 * magnitude2)
            val angleRad = acos(cosTheta).toDouble()
            var angleDeg = Math.toDegrees(angleRad)

            if (angleDeg < 0){
                angleDeg += 360
                if (angleDeg > 180){
                    angleDeg = 360 - angleDeg
                }
            }
            else if(angleDeg > 180){
                angleDeg = 360 - angleDeg
            }
            return angleDeg
        } else {
            return 0.0
        }

    }
    fun interp(x: Double, xMin: Double, xMax: Double, yMin: Double, yMax: Double): Double {
        return (yMin + (yMax - yMin) * ((x - xMin) / (xMax - xMin)))
    }
    fun interp(value: Double, fromRange: Pair<Double, Double>, toRange: Pair<Double, Double>): Double {
        val (x0, x1) = fromRange
        val (y0, y1) = toRange

        // Si el valor está fuera del rango de origen, devolvemos el valor correspondiente del rango destino
        if (value <= x0) return y0
        if (value >= x1) return y1

        // Realizamos la interpolación lineal
        val interpolation = y0 + (value - x0) * (y1 - y0) / (x1 - x0)

        return interpolation
    }
    fun loadJSONFromAsset(context: Context, filename: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = context.assets.open(filename)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}