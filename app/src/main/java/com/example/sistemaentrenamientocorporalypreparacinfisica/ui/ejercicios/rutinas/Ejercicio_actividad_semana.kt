package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentEjercicioActividadSemanaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio.LivePreviewActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios.TodosLosEjercicio
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class Ejercicio_actividad_semana : Fragment() {

    private var _binding: FragmentEjercicioActividadSemanaBinding? = null
    private val binding get() = _binding!!
    private val args: Ejercicio_actividad_semanaArgs by navArgs()
    private lateinit var viewModel: EjercicioActividadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEjercicioActividadSemanaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar ViewModel con Factory
        val factory = RutinaViewModelFactory(args.idUser.toString(), requireContext())
        viewModel = ViewModelProvider(this, factory).get(EjercicioActividadViewModel::class.java)

        // Observamos objetivos y repeticiones para hacer la predicción
        viewModel.objetivosUsuarios.observe(viewLifecycleOwner) { objetivos ->
            viewModel.repeticionesUsuarios.observe(viewLifecycleOwner) { repeticiones ->
                viewModel.listaejercicios.observe(viewLifecycleOwner) { listaEjercicio ->

                    val reps = repeticiones.map { it.totalRepeticiones }.toFloatArray()
                    val goals = objetivos.map { it.tieneRelacion }.toFloatArray()
                    val maxLength = 16

                    try {
                        val predictions = predict(requireContext(), reps, goals, maxLength)
                        val data = mutableListOf<Float>()
                        predictions?.let {
                            for (i in it.indices step 2) {
                                val recommendedReps = it[i]
                                val goalAchieved = it[i + 1]
                                data.add(recommendedReps)
                                Log.d("Resultados", "Ejercicio ${(i / 2) + 1}: Reps = $recommendedReps, Objetivo logrado = $goalAchieved")
                            }
                            viewModel.setdataReps(data)

                            initRecyclerView(listaEjercicio, data) { ejercicio ->
                                onItemSelected(ejercicio)
                            }

                        }
                    } catch (e: Exception) {
                        Log.e("prediction", "Error al realizar la predicción: ${e.message}")
                    }
                }
            }
        }

        return root
    }

    private fun initRecyclerView(lista: List<Ejercicio>, listaDatos: List<Float>, onClickListener: (Ejercicio) -> Unit) {
        binding.recyclerEjercicios2.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjercicios2.adapter = Ejercicio_actividad_adapter(lista, listaDatos, onClickListener)
    }


    private fun onItemSelected(it: Ejercicio) {
        val intent = Intent(requireContext(), LivePreviewActivity::class.java)
        intent.putExtra("id_ejercicio", it.idEjercicio.toString())
        intent.putExtra("name_ejercicio", it.nombre)
        Log.d(TodosLosEjercicio.TAG, "Datos ejercicios: ${it.idEjercicio}, ${it.nombre}")
        startActivity(intent)
    }

    // Funciones de carga y predicción TFLite siguen igual
    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun predict(context: Context, reps: FloatArray, goals: FloatArray, maxLength: Int): FloatArray? {
        val modelPath = "exercise_model_fixed.tflite"
        val tflite = Interpreter(loadModelFile(context, modelPath))
        val input = Array(1) { Array(maxLength) { FloatArray(2) } }
        for (i in reps.indices) {
            input[0][i][0] = reps[i]
            input[0][i][1] = goals.getOrNull(i) ?: 0f
        }
        val output = Array(1) { FloatArray(maxLength * 2) }
        tflite.run(input, output)
        tflite.close()
        return output[0]
    }
}

class RutinaViewModelFactory(
    private val idUser: String,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EjercicioActividadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EjercicioActividadViewModel(idUser, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
