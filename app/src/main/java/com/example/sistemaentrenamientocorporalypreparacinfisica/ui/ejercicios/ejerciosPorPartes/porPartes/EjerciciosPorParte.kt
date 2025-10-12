package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes.porPartes

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
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentEjerciciosPorParteBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.iniciarEjercicio.LivePreviewActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.todosLosEjercicios.TodosLosEjercicio




class EjerciciosPorParte : Fragment() {

    private var _binding: FragmentEjerciciosPorParteBinding? = null
    private lateinit var viewModel:EjerciciosPorParteViewModel
    private val binding get() = _binding!!
    private val args:EjerciciosPorParteArgs by navArgs()
    private lateinit var ruta:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ruta = MainActivity.geURLimage()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEjerciciosPorParteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainActivity = requireActivity() as MainActivity
        mainActivity.showBottomNav()

        val factory = EjerciciosPorParteViewModelFactory(args.idParteTrabajo, requireContext())
        viewModel = ViewModelProvider(this, factory).get(EjerciciosPorParteViewModel::class.java)

        viewModel.listaejercicios.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })

        return root
    }

    private fun initRecyclerView(lista: List<Ejercicio>) {
        binding.recyclerEjerciciosPartes.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjerciciosPartes.adapter = EjerciciosPorParteAdapter(lista) {
            onItemSelected(it)
        }
    }

    private fun onItemSelected(it: Ejercicio) {
        val intent = Intent(requireContext(), LivePreviewActivity::class.java)
        intent.putExtra("id_ejercicio",it.idEjercicio.toString())
        intent.putExtra("name_ejercicio",it.nombre)
        Log.d(TodosLosEjercicio.TAG,"Datos ejercicios:" + it.idEjercicio + " , " + it.nombre)
        startActivity(intent)
    }
}
class EjerciciosPorParteViewModelFactory(
    private val idParteTrabajo: Int,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EjerciciosPorParteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EjerciciosPorParteViewModel(idParteTrabajo, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
