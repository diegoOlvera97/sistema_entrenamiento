package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.CameraSource
import com.example.sistemaentrenamientocorporalypreparacinfisica.CameraSourcePreview
import com.example.sistemaentrenamientocorporalypreparacinfisica.GraphicOverlay
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.AbNormales
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.AbsWorOut
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.Burpees
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.ContextoEstrategias
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.CurlBicep
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.FlexionesDiamante
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.Fondos
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.JumpingJack
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.LegRaises
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.Lunges
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.MountainClimber
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.PatadaDeTricep
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.PushUp
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.RemoBarra
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.ReverseLunge
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.Sentadilla
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.SubidaEscalon
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasEjercicio.Wipers
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.example.sistemaentrenamientocorporalypreparacinfisica.posedetector.PoseDetectorProcessor
import com.example.sistemaentrenamientocorporalypreparacinfisica.preferences.PreferenceUtils
import com.example.sistemaentrenamientocorporalypreparacinfisica.preferences.SettingsActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.preferences.SettingsActivity.LaunchSource
import com.google.android.gms.common.annotation.KeepName
import kotlinx.coroutines.launch
import java.io.IOException

@KeepName
class

LivePreviewActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = POSE_DETECTION
    private lateinit var contextoEstrategias: ContextoEstrategias
    private lateinit var poseDetectorProcessor: PoseDetectorProcessor
    private lateinit var viewModel: LivePreviewActivityViewModel
    private val tiempoInicio = System.currentTimeMillis()
    private var idUser: String? = null
    private var idEjercicio: String? = null
    private var nombreEjercicio: String? = null
    private var datosEjercicioActual = DatosEjercicioActual(0.0, 0.0, 0.0, 0, "")
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_vision_live_preview)

        idEjercicio = intent.getStringExtra("id_ejercicio")
        nombreEjercicio = intent.getStringExtra("name_ejercicio")
        Log.d(TAG, "ID Ejercicio recibido: $idEjercicio")
        lifecycleScope.launch {
            idUser = MainActivity.getIdUser(this@LivePreviewActivity)
        }

        val factory = LivePreviewActivityViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(LivePreviewActivityViewModel::class.java)

        preview = findViewById(R.id.preview_view)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        facingSwitch.setOnCheckedChangeListener(this)

        val settingsButton = findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW)
            startActivity(intent)
        }

        // Verificar y solicitar permiso de cámara
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Se requiere el permiso de cámara para continuar", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        createCameraSource(selectedModel)
        startCameraSource()
    }

    override fun onBackPressed() {
        if (idEjercicio == null) {
            Log.e(TAG, "Error: idEjercicio es null")
            Toast.makeText(this, "Error: ID del ejercicio no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            idUser = idUser ?: MainActivity.getIdUser(this@LivePreviewActivity)

            val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio
            val tiempoTranscurridoSegundos = tiempoTranscurrido / 1000
            Log.d(TAG, "Repeticiones ${PoseDetectorProcessor.classificationResultData?.get(0)}")

            val builder = AlertDialog.Builder(this@LivePreviewActivity)
            builder.setMessage("¿Quieres salir de la aplicación?")
                .setPositiveButton("Sí") { _, _ ->
                    if (idUser != null) {
                        viewModel.procedimiento_datos_usuario_ejercicio_realizado(
                            idEjercicio!!,
                            idUser!!,
                            PoseDetectorProcessor.classificationResultData?.get(0).toString(),
                            tiempoTranscurridoSegundos.toString()
                        ) { success ->
                            if (success) {
                                Log.d(TAG, "✅ DATOS enviados exitosamente.")
                            } else {
                                Log.e(TAG, "❌ Falló el envío de datos")
                            }
                            // <-- salir solo cuando termine
                            super.onBackPressed()
                        }
                    } else {
                        Log.e(TAG, "Error: userID es null")
                        Toast.makeText(
                            this@LivePreviewActivity,
                            "Error al obtener el ID de user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }
    }


    @Synchronized
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        selectedModel = parent?.getItemAtPosition(pos).toString()
        Log.d(TAG, "Selected model: $selectedModel")
        preview?.stop()
        createCameraSource(selectedModel)
        startCameraSource()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing.
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Log.d(TAG, "Set facing")
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        preview?.stop()
        startCameraSource()
    }

    private fun createCameraSource(model: String) {
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }
        try {
            when (model) {
                POSE_DETECTION -> {
                    val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
                    Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
                    val shouldShowInFrameLikelihood =
                        PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
                    val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
                    val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
                    val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)
                    Log.d("LivePreviewActivity", "Inicializando PoseDetectorProcessor con runClassification=$runClassification")
                    val tipoDeEjercicio = when (nombreEjercicio) {
                        "Push Ups" -> PushUp(datosEjercicioActual)
                        "Burpees" -> Burpees(datosEjercicioActual)
                        "Jumping Jack" -> JumpingJack(datosEjercicioActual)
                        "Lunges" -> Lunges(datosEjercicioActual)
                        "Mountain Climber" -> MountainClimber(datosEjercicioActual)
                        "Squad" -> Sentadilla(datosEjercicioActual)
                        "Jumping Jack 2" -> AbsWorOut(datosEjercicioActual)
                        "Fondos Banco" -> Fondos(datosEjercicioActual)
                        "Sentadilla" -> Sentadilla(datosEjercicioActual)
                        "Patada de tricep" -> PatadaDeTricep(datosEjercicioActual)
                        "Flexiones Diamante" -> FlexionesDiamante(datosEjercicioActual)
                        "Reverse Lunge" -> ReverseLunge(datosEjercicioActual)
                        "Subida de escalón" -> SubidaEscalon(datosEjercicioActual)
                        "Wipers" -> Wipers(datosEjercicioActual)
                        "Leg Raises" -> LegRaises(datosEjercicioActual)
                        "Abdominales Normales" -> AbNormales(datosEjercicioActual)
                        "Remo Barra" -> RemoBarra(datosEjercicioActual)
                        "Curl Bicep" -> CurlBicep(datosEjercicioActual)
                        else -> throw IllegalArgumentException("Opción inválida")
                    }
                    contextoEstrategias = ContextoEstrategias(tipoDeEjercicio)

                    poseDetectorProcessor = PoseDetectorProcessor(
                        this,
                        poseDetectorOptions,
                        shouldShowInFrameLikelihood,
                        visualizeZ,
                        rescaleZ,
                        runClassification,
                        true,
                        contextoEstrategias
                    )
                    Log.d("LivePreviewActivity", "Frame recibido para análisis (se enviará a PoseDetectorProcessor)")
                    cameraSource!!.setMachineLearningFrameProcessor(
                        poseDetectorProcessor
                    )
                }
                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $model", e)
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource?.release()
        }
    }

    class LivePreviewActivityViewModelFactory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LivePreviewActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LivePreviewActivityViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private const val POSE_DETECTION = "Pose Detection"
        private const val TAG = "LivePreviewActivity"
    }
}