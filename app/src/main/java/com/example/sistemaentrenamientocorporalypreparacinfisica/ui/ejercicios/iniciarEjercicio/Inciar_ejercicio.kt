package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.PoseLandmarkerHelper
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentInciarEjercicioBinding
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
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces.ContextEstrategiasSonido
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces.VozHombre
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces.VozMujer
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.interfaces.TipoDeVoz
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DataSesion
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DatosEjercicioActual
import com.example.sistemaentrenamientocorporalypreparacinfisica.modelView.IniciarEjercicioViewModel
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas.PermissionsFragment
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Inciar_ejercicio : Fragment() , PoseLandmarkerHelper.LandmarkerListener{

    companion object {
        private const val TAG = "Pose Landmarker"
    }

    private val args:Inciar_ejercicioArgs by navArgs()
    private var _iniciarEjercicioBinding: FragmentInciarEjercicioBinding? = null

    private val fragmentCameraBinding
        get() = _iniciarEjercicioBinding!!


    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper
    private val viewModel: IniciarEjercicioViewModel by activityViewModels()

    var datosEjercicioActual = DatosEjercicioActual(0.0,0.0,0.0,0,"")
    private var mediaPlayer: MediaPlayer? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraFacing = CameraSelector.LENS_FACING_BACK

    private lateinit var tipoDeEjercicio: TipoDeEjercicio
    private lateinit var tipoDeVoz:TipoDeVoz
    private lateinit var angulosJson:String
    private lateinit var contextoEstrategias: ContextoEstrategias
    private lateinit var contextoEstrategiasSonido: ContextEstrategiasSonido
    private var dataSesion:DataSesion? =null
    private val tiempoInicio = System.currentTimeMillis()
    private var banderaIndicacion = ""


    private var currentIndex = 0
    private lateinit var imageView: ImageView

    private val handler = Handler()
    private lateinit var imageResources:List<Int>

    private val imageSwitcherRunnable = object : Runnable {
        override fun run() {
            // Cambiar la imagen cada 10 segundos
            currentIndex = (currentIndex + 1) % imageResources.size
            // Actualizar la imagen en el ImageView
            //imageView.setImageResource(imageResources[currentIndex])
            fragmentCameraBinding.imagenGIF.setImageResource(imageResources[currentIndex])
            // Programar la ejecución del Runnable nuevamente después de 10 segundos
            //handler.postDelayed(this, 1000) // 10000 milisegundos = 10 segundos
        }
    }

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ExecutorService

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_main
            ).navigate(R.id.action_inciar_ejercicio_to_permissionsFragment)
        }

        // Start the PoseLandmarkerHelper again when users come back
        // to the foreground.
        backgroundExecutor.execute {
            if(this::poseLandmarkerHelper.isInitialized) {
                if (poseLandmarkerHelper.isClose()) {
                    poseLandmarkerHelper.setupPoseLandmarker()
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        if(this::poseLandmarkerHelper.isInitialized) {
            viewModel.setMinPoseDetectionConfidence(poseLandmarkerHelper.minPoseDetectionConfidence)
            viewModel.setMinPoseTrackingConfidence(poseLandmarkerHelper.minPoseTrackingConfidence)
            viewModel.setMinPosePresenceConfidence(poseLandmarkerHelper.minPosePresenceConfidence)
            viewModel.setDelegate(poseLandmarkerHelper.currentDelegate)

            // Close the PoseLandmarkerHelper and release resources
            backgroundExecutor.execute { poseLandmarkerHelper.clearPoseLandmarker() }
        }
    }
    override fun onDestroyView() {
        _iniciarEjercicioBinding = null
        super.onDestroyView()

        // Shut down our background executor
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
        mediaPlayer?.release()
        mediaPlayer = null

        handler.removeCallbacks(imageSwitcherRunnable)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _iniciarEjercicioBinding =
            FragmentInciarEjercicioBinding.inflate(inflater, container, false)

        // Esconde tanto el toolActionBar como el nav_navigation_bar
        (requireActivity() as MainActivity).supportActionBar!!.hide()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideBottomNav()


        /**
         * Cambia la camara
         */
        fragmentCameraBinding.chageCamera.setOnClickListener {
            // Cambia la dirección de la cámara
            cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            // Reconfigura las cámaras
            bindCameraUseCases()
        }

        /**
         * Guarda los datos del usario cuando este vuelve a la lista de ejercicios
         */
        //Log.d("CATLOGIN","IDUSER JSJSJSJJSJ: " + args.idUser)
        // Modificar para mostrar pantalla de sis esta seguro de salir
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    // Muestra un cuadro de diálogo de confirmación
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("¿Estás seguro que deseas salir?")
                        .setCancelable(false)
                        .setPositiveButton("Sí") { dialog, _ ->
                            // Si el usuario confirma que quiere salir, llama a onBackPressed()
                            dialog.dismiss()
                            if(datosEjercicioActual.count > 0.0){
                                /**
                                 * REALIZA INSERCION O UPDATE DEL EJERCICIO REALIZADO
                                 */

                                // LLama a un procedimiento almacenado que actualiza o insert datos
                                val requestQueue = Volley.newRequestQueue(context)
                                val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio
                                val tiempoTrancurridoSegundos = tiempoTranscurrido/1000
                                val url = MainActivity.getURL() + "procedimiento_datos_usuario_ejercicio_realizado.php"
                                val stringRequest = object: StringRequest(Method.POST,url,
                                    Response.Listener{ response ->
                                        Log.d("CALOGIN", response)
                                    }, Response.ErrorListener { error->
                                        Log.d("CALOGIN", "No se logro conectar sqL cameraFragment: " + error)
                                    }){
                                    override fun getParams(): MutableMap<String, String>? {
                                        val params = HashMap<String, String>()
                                        params.put("idEjercicio",args.idEjer.toString())
                                        params.put("idUser", args.idUser.toString())
                                        params.put("repeticiones",datosEjercicioActual.count.toString())
                                        params.put("tiempo",tiempoTrancurridoSegundos.toString())
                                        return params
                                    }
                                }
                                requestQueue.add(stringRequest)
                            }

                            (requireActivity() as MainActivity).supportActionBar!!.show()
                            if (isEnabled) {
                                isEnabled = false
                                findNavController().popBackStack()
                            }

                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Si el usuario elige no salir, simplemente cierra el cuadro de diálogo
                            dialog.dismiss()
                        }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }
            )


        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Interfaz de voz
        val choice = args.tipoVoz
        tipoDeVoz = when(choice){
            "Hombre" -> VozHombre(requireContext())
            "Mujer" -> VozMujer(requireContext())
            else -> throw IllegalArgumentException("Opción inválida")
        }
        contextoEstrategiasSonido = ContextEstrategiasSonido(tipoDeVoz)

        tipoDeEjercicio = when(args.nombreEjercicio){
            "Push Ups" -> PushUp(datosEjercicioActual)
            "Burpees"-> Burpees(datosEjercicioActual)
            "Jumping Jack"-> JumpingJack(datosEjercicioActual)
            "Lunges"-> Lunges(datosEjercicioActual)
            "Mountain Climber"-> MountainClimber(datosEjercicioActual)
            "Squad"-> Sentadilla(datosEjercicioActual)
            "Jumping Jack 2" -> AbsWorOut(datosEjercicioActual)
            "Fondos Banco" -> Fondos(datosEjercicioActual)
            "Sentadilla" -> Sentadilla(datosEjercicioActual)
            "Patada de tricep" ->PatadaDeTricep(datosEjercicioActual)
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
        val cadenaJson = when(args.nombreEjercicio){
            "Jumping Jack 2"    -> loadJSONFromAsset("fileJson/jumping.json")
            "Fondos Banco"            -> loadJSONFromAsset("fileJson/fondos.json")
            "Sentadilla"        -> loadJSONFromAsset("fileJson/sentadillas.json")
            "Patada de tricep"  -> loadJSONFromAsset("fileJson/patadatriceps.json")
            "Flexiones Diamante" -> loadJSONFromAsset("fileJson/flexionesdiamante.json")
            "Reverse Lunge" -> loadJSONFromAsset("fileJson/reverselunge.json")
            "Subida de escalón" -> loadJSONFromAsset("fileJson/upescalon.json")
            "Wipers" -> loadJSONFromAsset("fileJson/wipers.json")
            "Leg Raises" -> loadJSONFromAsset("fileJson/legraises.json")
            "Abdominales Normales" -> loadJSONFromAsset("fileJson/adnormales.json")
            "Burpees" -> loadJSONFromAsset("fileJson/burpee.json")
            "Remo Barra" ->loadJSONFromAsset("fileJson/remobarra.json")
            "Curl Bicep" ->loadJSONFromAsset("fileJson/curlbicep.json")
            "Squad"->loadJSONFromAsset("fileJson/sentadillas.json")
            else -> "Hola"
        }
        angulosJson = cadenaJson.toString()

        Log.d("LORE",angulosJson)

        contextoEstrategias = ContextoEstrategias(tipoDeEjercicio)

        //imageView = fragmentCameraBinding.imagenCambiante
        imageResources = contextoEstrategias.executegetListImage()
        startImageSwitcher()
        // Initialize our background executor
        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        fragmentCameraBinding.viewFinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

        // Create the PoseLandmarkerHelper that will handle the inference
        backgroundExecutor.execute {
            poseLandmarkerHelper = PoseLandmarkerHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                minPoseDetectionConfidence = viewModel.currentMinPoseDetectionConfidence,
                minPoseTrackingConfidence = viewModel.currentMinPoseTrackingConfidence,
                minPosePresenceConfidence = viewModel.currentMinPosePresenceConfidence,
                currentDelegate = viewModel.currentDelegate,
                poseLandmarkerHelperListener = this
            )
        }

        // Attach listeners to UI control widgets
        initBottomSheetControls()
        //updateControlsUi()

    }

    private fun initBottomSheetControls() {
        // init bottom sheet settings

        fragmentCameraBinding.bottomSheetLayout.detectionThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinPoseDetectionConfidence
            )
        fragmentCameraBinding.bottomSheetLayout.trackingThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinPoseTrackingConfidence
            )
        fragmentCameraBinding.bottomSheetLayout.presenceThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinPosePresenceConfidence
            )

        // When clicked, lower pose detection score threshold floor
        fragmentCameraBinding.bottomSheetLayout.detectionThresholdMinus.setOnClickListener {
            if (poseLandmarkerHelper.minPoseDetectionConfidence >= 0.2) {
                poseLandmarkerHelper.minPoseDetectionConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise pose detection score threshold floor
        fragmentCameraBinding.bottomSheetLayout.detectionThresholdPlus.setOnClickListener {
            if (poseLandmarkerHelper.minPoseDetectionConfidence <= 0.8) {
                poseLandmarkerHelper.minPoseDetectionConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, lower pose tracking score threshold floor
        fragmentCameraBinding.bottomSheetLayout.trackingThresholdMinus.setOnClickListener {
            if (poseLandmarkerHelper.minPoseTrackingConfidence >= 0.2) {
                poseLandmarkerHelper.minPoseTrackingConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise pose tracking score threshold floor
        fragmentCameraBinding.bottomSheetLayout.trackingThresholdPlus.setOnClickListener {
            if (poseLandmarkerHelper.minPoseTrackingConfidence <= 0.8) {
                poseLandmarkerHelper.minPoseTrackingConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, lower pose presence score threshold floor
        fragmentCameraBinding.bottomSheetLayout.presenceThresholdMinus.setOnClickListener {
            if (poseLandmarkerHelper.minPosePresenceConfidence >= 0.2) {
                poseLandmarkerHelper.minPosePresenceConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise pose presence score threshold floor
        fragmentCameraBinding.bottomSheetLayout.presenceThresholdPlus.setOnClickListener {
            if (poseLandmarkerHelper.minPosePresenceConfidence <= 0.8) {
                poseLandmarkerHelper.minPosePresenceConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, change the underlying hardware used for inference.
        // Current options are CPU and GPU
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(
            viewModel.currentDelegate, false
        )
        fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long
                ) {
                    try {
                        poseLandmarkerHelper.currentDelegate = p2
                        updateControlsUi()
                    } catch(e: UninitializedPropertyAccessException) {
                        Log.e(TAG, "PoseLandmarkerHelper has not been initialized yet.")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }

        // When clicked, change the underlying model used for object detection
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.setSelection(
            viewModel.currentModel,
            false
        )
        fragmentCameraBinding.bottomSheetLayout.spinnerModel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    poseLandmarkerHelper.currentModel = p2
                    updateControlsUi()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }
    }
    // Update the values displayed in the bottom sheet. Reset Poselandmarker
    // helper.
    private fun updateControlsUi() {
        if(this::poseLandmarkerHelper.isInitialized) {
            fragmentCameraBinding.bottomSheetLayout.detectionThresholdValue.text =
                String.format(
                    Locale.US,
                    "%.2f",
                    poseLandmarkerHelper.minPoseDetectionConfidence
                )
            fragmentCameraBinding.bottomSheetLayout.trackingThresholdValue.text =
                String.format(
                    Locale.US,
                    "%.2f",
                    poseLandmarkerHelper.minPoseTrackingConfidence
                )
            fragmentCameraBinding.bottomSheetLayout.presenceThresholdValue.text =
                String.format(
                    Locale.US,
                    "%.2f",
                    poseLandmarkerHelper.minPosePresenceConfidence
                )

            // Needs to be cleared instead of reinitialized because the GPU
            // delegate needs to be initialized on the thread using it when applicable
            backgroundExecutor.execute {
                poseLandmarkerHelper.clearPoseLandmarker()
                poseLandmarkerHelper.setupPoseLandmarker()
            }
            fragmentCameraBinding.overlay.clear()
        }
    }
    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(requireContext())
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
            .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        detectPose(image)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }
    private fun detectPose(imageProxy: ImageProxy) {
        if(this::poseLandmarkerHelper.isInitialized) {
            poseLandmarkerHelper.detectLiveStream(
                imageProxy = imageProxy,
                isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
            )
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            fragmentCameraBinding.viewFinder.display.rotation
    }
    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            if (errorCode == PoseLandmarkerHelper.GPU_ERROR) {
                fragmentCameraBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                    PoseLandmarkerHelper.DELEGATE_CPU, false
                )
            }
        }
    }

    override fun onResults(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        activity?.runOnUiThread {
            val poseLandmarkerResults = resultBundle.results.first()
            datosEjercicioActual = contextoEstrategias.executeanalyzeDataEjercicio(poseLandmarkerResults.landmarks(),angulosJson)

            if(args.habilitarVoz){
                if( datosEjercicioActual.feeback != banderaIndicacion){
                    mediaPlayer = MediaPlayer.create(requireContext(),contextoEstrategiasSonido.excuteSonido(datosEjercicioActual.feeback))
                    playSound()
                    /*
                    if(args.nombreEjercicio == "Burpees"){
                        banderaIndicacion = datosEjercicioActual.feeback
                    }

                    banderaIndicacion = datosEjercicioActual.feeback

                     */
                }
            }
            if (_iniciarEjercicioBinding != null) {
                // Pass necessary information to OverlayView for drawing on the canvas
                fragmentCameraBinding.overlay.setResults(
                    poseLandmarkerResults,
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM,
                    datosEjercicioActual,
                    args.nombreEjercicio
                )

                // Force a redraw
                fragmentCameraBinding.overlay.invalidate()
            }
        }
    }
    private fun playSound() {
        // Verificar si el MediaPlayer está nulo o ya está reproduciendo
        mediaPlayer?.let {
            if (!it.isPlaying) {
                // Iniciar la reproducción del sonido
                it.start()
            }
        }
    }

    private fun startImageSwitcher() {
        // Iniciar la secuencia de imágenes
        handler.post(imageSwitcherRunnable)
    }

    private fun loadJSONFromAsset(filename: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = requireContext().assets.open(filename)
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