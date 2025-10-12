package com.example.sistemaentrenamientocorporalypreparacinfisica.posedetector

import android.content.Context
import android.util.Log
import com.example.sistemaentrenamientocorporalypreparacinfisica.GraphicOverlay
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.ContextoEstrategias
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.posedetector.classification.PoseClassifierProcessor
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio.VisionProcessorBase
import com.google.android.gms.tasks.Task
import com.google.android.odml.image.MlImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import java.util.ArrayList
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PoseDetectorProcessor(
    private val context: Context,
    options: PoseDetectorOptionsBase,
    private val showInFrameLikelihood: Boolean,
    private val visualizeZ: Boolean,
    private val rescaleZForVisualization: Boolean,
    private val runClassification: Boolean,
    private val isStreamMode: Boolean,
    private val contextoEstrategia:ContextoEstrategias
): VisionProcessorBase<PoseDetectorProcessor.PoseWithClassification>(context) {

    private val detector: PoseDetector
    private val classificationExecutor: Executor
    private var poseClassifierProcessor: PoseClassifierProcessor? = null

    /** Internal class to hold Pose and classification results. */
    class PoseWithClassification(val pose: Pose, val classificationResult: List<String>)

    init {
        detector = PoseDetection.getClient(options)
        classificationExecutor = Executors.newSingleThreadExecutor()
    }

    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<PoseWithClassification> {
        Log.d(TAG, "detectInImage(InputImage) llamado")
        return detector
            .process(image)
            .addOnSuccessListener { Log.d(TAG, "Pose detectada en InputImage") }
            .addOnFailureListener { Log.e(TAG, "Error detectando pose en InputImage", it) }
            .continueWith(
                classificationExecutor,
                { task ->
                    val pose = task.getResult()
                    Log.d(TAG, "Pose procesada, landmarks=${pose.allPoseLandmarks.size}")
                    Log.d(TAG, "runClassification=$runClassification, poseClassifierProcessor=${poseClassifierProcessor != null}")
                    var classificationResult: List<String> = ArrayList()
                    if (runClassification) {
                        if (poseClassifierProcessor == null) {
                            Log.d(TAG, "Inicializando PoseClassifierProcessor")
                            poseClassifierProcessor = PoseClassifierProcessor(context, isStreamMode,contextoEstrategia)
                        }
                        classificationResult = poseClassifierProcessor!!.getPoseResult(pose)
                        classificationResultData = classificationResult.toList()
                        Log.d(TAG, "Resultados clasificación: $classificationResult")
                    }
                    PoseWithClassification(pose, classificationResult)
                }
            )
    }

    override fun detectInImage(image: MlImage): Task<PoseWithClassification> {
        Log.d(TAG, "detectInImage(InputImage) llamado")
        return detector
            .process(image)
            .addOnSuccessListener { Log.d(TAG, "Pose detectada en InputImage") }
            .addOnFailureListener { Log.e(TAG, "Error detectando pose en InputImage", it) }
            .continueWith(
                classificationExecutor,
                { task ->
                    val pose = task.getResult()
                    Log.d(TAG, "Pose procesada, landmarks=${pose.allPoseLandmarks.size}")
                    Log.d(TAG, "runClassification=$runClassification, poseClassifierProcessor=${poseClassifierProcessor != null}")
                    var classificationResult: List<String> = ArrayList()
                    if (runClassification) {
                        if (poseClassifierProcessor == null) {
                            Log.d(TAG, "Inicializando PoseClassifierProcessor")
                            poseClassifierProcessor = PoseClassifierProcessor(context, isStreamMode,contextoEstrategia)
                        }
                        classificationResult = poseClassifierProcessor!!.getPoseResult(pose)
                        classificationResultData = classificationResult.toList()
                        Log.d(TAG, "Resultados clasificación: $classificationResult")
                    }
                    PoseWithClassification(pose, classificationResult)
                }
            )
    }

    override fun onSuccess(
        poseWithClassification: PoseWithClassification,
        graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.add(
            PoseGraphic(
                graphicOverlay,
                poseWithClassification.pose,
                showInFrameLikelihood,
                visualizeZ,
                rescaleZForVisualization,
                contextoEstrategia.getNombreEjercicio(),
                poseWithClassification.classificationResult,
                context
            )
        )
    }



    override fun onFailure(e: Exception) {
        Log.e(TAG, "Pose detection failed!", e)
    }

    override fun isMlImageEnabled(context: Context?): Boolean {
        // Use MlImage in Pose Detection by default, change it to OFF to switch to InputImage.
        return true
    }

    companion object {
        private val TAG = "PoseDetectorProcessor"
        var classificationResultData: List<String>? = null
    }

}