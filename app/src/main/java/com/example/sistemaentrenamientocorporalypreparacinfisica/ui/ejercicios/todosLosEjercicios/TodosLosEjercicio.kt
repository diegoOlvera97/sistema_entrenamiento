package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentTodosLosEjercicioBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio.LivePreviewActivity

class TodosLosEjercicio : Fragment() {

    private var _binding: FragmentTodosLosEjercicioBinding? = null
    private lateinit var viewModel: TodosLosEjerciciosViewModel
    private val args:TodosLosEjercicioArgs by navArgs()
    private val binding get() = _binding!!
    private lateinit var ruta:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ruta = MainActivity.geURLimage()
        val factory = TodosLosEjerciciosViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this,factory).get(TodosLosEjerciciosViewModel::class.java)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodosLosEjercicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.listaejercicios.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
        return root
    }
    private fun onItemSelected(it: Ejercicio) {
        val intent = Intent(requireContext(), LivePreviewActivity::class.java)
        intent.putExtra("id_ejercicio",it.idEjercicio.toString())
        intent.putExtra("name_ejercicio",it.nombre)
        Log.d(TAG,"Datos ejercicios:" + it.idEjercicio + " , " + it.nombre)
        startActivity(intent)
    }

    private fun initRecyclerView(lista: List<Ejercicio>) {
        binding.recyclerEjercicios.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjercicios.adapter = TodosLosEjercicioAdapter(lista) {
            onItemSelected(it)
        }
    }

    companion object{
        val TAG = "TodosLosEjercicios"
    }
}
class TodosLosEjerciciosViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodosLosEjerciciosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodosLosEjerciciosViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

