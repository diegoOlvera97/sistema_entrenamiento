package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentInciarEjercicioBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs){

    private var results: PoseLandmarkerResult? = null
    private var pointPaint = Paint()
    private var linePaint = Paint()
    private var textPaint = Paint()
    private var textPaintContador = Paint()
    private var rectPaint = Paint()
    private var rectPaintSucces = Paint()
    private var rectPaintBackGround = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    // Cordenadas de las distintas componentes de la interfas
    private val contadorX = 0.05f // coordenada x del texto
    private val contadorY = 0.9f // coordenada y del texto
    private val tituloX = 0.05f
    private val tituloY = 0.05f
    private val mensajeX = 0.05f
    private val mensajeY = 0.1f
    private val errorX = 0.5f
    private val errorY = 0.9f

    //Datos del ejercicio realizado
    private var datosEjercicioActual = DatosEjercicioActual(0.0,0.0,1.0,1,"")
    private lateinit var nombreEjercicio:String

    //Datos ejercicio
    private var countReps = 0
    private var countTime = 0
    //Rectangulos
    // Rectangulo blanco
    private val precisionX = 0.0f
    private val precisionY = 0.02f
    private val precisionEndX = 0.05f
    private val precisionEndY =0.8f
    //Rectangulol verde
    private val precisionSX = 0.01f
    private val precisionSY = 0.025f
    private val precisionEndSX = 0.04f
    private val precisionEndSY =0.795f



    init {
        initPaints()
    }

    fun clear() {
        results = null
        pointPaint.reset()
        linePaint.reset()
        textPaint.reset()
        rectPaint.reset()
        rectPaintBackGround.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        linePaint.color =
            ContextCompat.getColor(context!!, R.color.purple_500)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL

        textPaint.color = Color.WHITE
        textPaint.textSize = 50f
        val typeface = ResourcesCompat.getFont(context, R.font.oswald_variablefont_wght)
        textPaint.setTypeface(typeface)

        textPaintContador.color = Color.WHITE
        textPaintContador.textSize = 140f

        rectPaint.color = Color.WHITE
        rectPaint.style = Paint.Style.FILL

        rectPaintBackGround.color = ContextCompat.getColor(context!!, R.color.purple_500)
        rectPaintBackGround.style = Paint.Style.FILL

        rectPaintSucces.color = Color.GREEN
        rectPaintSucces.style = Paint.Style.FILL

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { poseLandmarkerResult ->
            for(landmark in poseLandmarkerResult.landmarks()) {
                for(normalizedLandmark in landmark) {
                    canvas.drawPoint(
                        normalizedLandmark.x() * imageWidth * scaleFactor,
                        normalizedLandmark.y() * imageHeight * scaleFactor,
                        pointPaint
                    )
                }

                PoseLandmarker.POSE_LANDMARKS.forEach {
                    canvas.drawLine(
                        poseLandmarkerResult.landmarks().get(0).get(it!!.start()).x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.start()).y() * imageHeight * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.end()).x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.end()).y() * imageHeight * scaleFactor,
                        linePaint)
                    /*
                    //Dibuja los datos de cada landmark
                    canvas.drawText(
                        it.toString(),
                        poseLandmarkerResult.landmarks().get(0).get(it!!.start()).x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.start()).y() * imageHeight * scaleFactor,
                        textPaint
                    )
                     */
                }
            }

            //BackGorund Ejercicio
            // Dibujar rectPaintBackGround debajo del título con un padding de 5dp
            val tituloRectTop = (tituloX + 0.01f) * imageHeight * scaleFactor
            val tituloRectBottom = (tituloX - .03f) * imageHeight * scaleFactor // Altura del texto del título
            val tituloRectLeft = tituloY * imageWidth * scaleFactor
            val tituloRectRight = (tituloY + .25f) * imageWidth * scaleFactor  // Ancho del texto del título

            canvas.drawRect(
                tituloRectLeft,
                tituloRectTop,
                tituloRectRight,
                tituloRectBottom,
                rectPaintBackGround
            )

            // NOMBRE EJERCICIO
            canvas.drawText(
                nombreEjercicio,
                tituloX * imageWidth * scaleFactor,
                tituloY * imageHeight * scaleFactor,
                textPaint
            )

            canvas.drawRect(
                precisionX * imageWidth * scaleFactor,
                precisionY * imageHeight * scaleFactor,
                precisionEndX * imageWidth * scaleFactor,
                precisionEndY * imageHeight * scaleFactor,
                rectPaint
            )
            // MENSAJE DE RETROALIMENTACION
            canvas.drawText(
                datosEjercicioActual.feeback,
                mensajeX * imageWidth * scaleFactor,
                mensajeY * imageHeight * scaleFactor,
                textPaint
            )
            //TEXTO REPETICIONES
            canvas.drawText(
                "Repeticiones:",
                contadorX* imageWidth * scaleFactor,
                (contadorY - 0.08f) * imageHeight * scaleFactor,
                textPaint
            )
            // CUENTA DE LAS REPETICIONES

            canvas.drawText(
                datosEjercicioActual.count.toInt().toString(),
                contadorX* imageWidth * scaleFactor,
                contadorY * imageHeight * scaleFactor, //ANTES DE 0.2f habia un contador Y
                textPaintContador
            )
            // INDICADOR DE LA FORMA
            canvas.drawText(
                datosEjercicioActual.form.toString(),
                errorX* imageWidth * scaleFactor,
                errorY * imageHeight * scaleFactor,
                textPaintContador
            )

            // BARRA PORCENTAJE EXITO
            val perRect = precisionSY + (precisionEndSY-precisionSY)*(datosEjercicioActual.porcentajeSucces/100)
            //Rectangulo que indica el porcantaje de exito
            canvas.drawRect(
                precisionSX * imageWidth * scaleFactor,
                perRect.toFloat() * imageHeight * scaleFactor,
                precisionEndSX * imageWidth * scaleFactor,
                precisionEndSY * imageHeight * scaleFactor,
                rectPaintSucces
            )
            // Dibujar el texto usando las coordenadas obtenidas
            /*
            canvas.drawText(
                "[Grados: " + angle + "]",
                pruebasX * imageWidth * scaleFactor,
                pruebasY * imageHeight * scaleFactor,
                textPaint
            )
             */
            // Dibujar texto
        }

    }


    // Función para calcular el ángulo entre tres puntos
    private fun findAngle(a:Int, b:Int, c:Int,landmarksGraficos: MutableList<MutableList<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>>):Double{
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
        }
        if (angle > 180){
            angle = 360 - angle
        }else if(angle > 180){
            angle = 360 - angle
        }
        return angle
    }
    fun interp(x: Double, xMin: Double, xMax: Double, yMin: Double, yMax: Double): Float {
        return (yMin + (yMax - yMin) * ((x - xMin) / (xMax - xMin))).toFloat()
    }

    fun setResults(
        poseLandmarkerResults: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE,
        datosEjercicioActual: DatosEjercicioActual,
        nombreEjercicio:String
    ) {
        results = poseLandmarkerResults
        this.datosEjercicioActual = datosEjercicioActual
        this.nombreEjercicio = nombreEjercicio
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth


        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 12F
    }
}
