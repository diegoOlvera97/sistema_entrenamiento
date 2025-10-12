package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoColacion.tipocolacionlista

import android.content.Context
import android.os.Bundle
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
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentTipoColacionListaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var viewModel:TipoColacionListaViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TipoColacionLista.newInstance] factory method to
 * create an instance of this fragment.
 */
class TipoColacionLista : Fragment() {

    private var _binding: FragmentTipoColacionListaBinding? = null
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var url:String
    val args: TipoColacionListaArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        url = MainActivity.geURLimage()
        val factory = TipoColacionListaViewModelFactory(args.tipoColacion,requireContext())
        viewModel = ViewModelProvider(this, factory).get(TipoColacionListaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTipoColacionListaBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel.listaColacion.observe(viewLifecycleOwner, Observer {
            // init recycler view
            initRecyclerView(it)
        })

        return root
    }

    private fun initRecyclerView(lista: List<Colacion>) {
        binding.recyclerColacion.layoutManager = LinearLayoutManager(context)
        binding.recyclerColacion.adapter = TipoColacionListaAdapter(lista) {
            onItemSelected(it)
        }
    }

    private fun onItemSelected(colacion:Colacion) {
        findNavController().navigate(
            TipoColacionListaDirections.actionTipoColacionListaToColacion(
                idColacion = colacion.idColacion
            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TipoColacionLista.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TipoColacionLista().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class TipoColacionListaViewModelFactory(
    private val tipoColacion:String,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TipoColacionListaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TipoColacionListaViewModel(tipoColacion,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}