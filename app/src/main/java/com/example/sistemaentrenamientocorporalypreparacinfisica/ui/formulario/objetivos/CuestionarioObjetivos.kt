package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario.objetivos

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentSerieDatos2Binding
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo

class CuestionarioObjetivos : Fragment() {

    private lateinit var viewModel: CuestionarioObjetivosViewModel
    private var _binding: FragmentSerieDatos2Binding? = null
    val args: CuestionarioObjetivosArgs by navArgs()
    private val binding get() = _binding!!

    private var arregloObjetivos: MutableList<ParteTrabajoCuerpo> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ObjetivosViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(CuestionarioObjetivosViewModel::class.java)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSerieDatos2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.button4.setOnClickListener {
            val selectedOptions = arregloObjetivos.filter { it.isChecked }

            selectedOptions.forEachIndexed { index, option ->
                viewModel.insertarObjetivoUsuario(args.idUser, option.idParteTrabajo) { success ->
                    if (index == selectedOptions.lastIndex) {
                        findNavController().navigate(
                            CuestionarioObjetivosDirections.actionSerieDatos2ToAlergiasPantalla2(
                                idUser = args.idUser
                            )
                        )
                    }
                }
            }
        }

        return root
    }

    private fun observeViewModel() {
        viewModel.objetivos.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
            arregloObjetivos.clear()
            arregloObjetivos.addAll(it)
        })
    }

    private fun initRecyclerView(lista: List<ParteTrabajoCuerpo>) {
        binding.recyclerSerie2.layoutManager = LinearLayoutManager(context)
        binding.recyclerSerie2.adapter = CuestionarioObjetivosAdapter(lista)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ObjetivosViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CuestionarioObjetivosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CuestionarioObjetivosViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
