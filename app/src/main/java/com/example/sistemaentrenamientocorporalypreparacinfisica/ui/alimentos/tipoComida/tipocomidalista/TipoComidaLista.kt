package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoComida.tipocomidalista

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
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentTipoComidaListaBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Alimentos


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var viewModel:TipoComidaListaViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TipoComidaLista.newInstance] factory method to
 * create an instance of this fragment.
 */
class TipoComidaLista : Fragment() {
    private var _binding: FragmentTipoComidaListaBinding? = null
    val args:TipoComidaListaArgs by navArgs()
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        val factory = TipoComidaListaViewModelFactory(args.tipoComida,requireContext())
        viewModel = ViewModelProvider(this, factory).get(TipoComidaListaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTipoComidaListaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.listaComida.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
        return root
    }

    private fun initRecyclerView(lista: List<Alimentos>) {
        binding.recyclerEjercicios.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjercicios.adapter = TipoComidaListaAdapter(lista) {
            onItemSelected(it)
        }
    }

    private fun onItemSelected(alimento: Alimentos) {
        findNavController().navigate(
            TipoComidaListaDirections.actionTipoComidaListaToRecomentdacion(
                idAlimento = alimento.idAlimento
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
         * @return A new instance of fragment TipoComidaLista.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TipoComidaLista().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class TipoComidaListaViewModelFactory(
    private val tipoComida:String,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TipoComidaListaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TipoComidaListaViewModel(tipoComida) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}