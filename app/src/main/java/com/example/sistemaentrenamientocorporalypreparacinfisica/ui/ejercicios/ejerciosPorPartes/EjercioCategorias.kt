package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.ejerciosPorPartes

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
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentEjercioCategoriasBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo
import com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.EjerciciosAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [EjercioCategorias.newInstance] factory method to
 * create an instance of this fragment.
 */
class EjercioCategorias : Fragment() {
    private val args: EjercioCategoriasArgs by navArgs()
    private var _binding: FragmentEjercioCategoriasBinding? = null
    private var ejerciciosListaCuerpo:MutableList<ParteTrabajoCuerpo> = mutableListOf()
    private lateinit var viewModel: EjercicioCategoriasViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEjercioCategoriasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = EjercicioCategoriasViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this@EjercioCategorias, factory).get(EjercicioCategoriasViewModel::class.java)

        viewModel.listaCategorias.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
        return root
    }

    private fun initRecyclerView(parteTrabajo:List<ParteTrabajoCuerpo>) {
        binding.recyclerParteTrabajo.layoutManager = LinearLayoutManager(context)
        //binding.recyclerParteTrabajo.layoutManager = GridLayoutManager(context,2)

        binding.recyclerParteTrabajo.adapter = EjerciciosAdapter(parteTrabajo){
            onItemSelected(it)
        }
    }
    fun onItemSelected(sem: ParteTrabajoCuerpo){
        findNavController().navigate(
            EjercioCategoriasDirections.actionEjercioCategoriasToEjerciciosPorParte(
                idUser = args.idUser,
                idParteTrabajo = sem.idParteTrabajo // PASAMOS EL ID NUMÉRICO
            )
        )
    }

}

class EjercicioCategoriasViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EjercicioCategoriasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EjercicioCategoriasViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}