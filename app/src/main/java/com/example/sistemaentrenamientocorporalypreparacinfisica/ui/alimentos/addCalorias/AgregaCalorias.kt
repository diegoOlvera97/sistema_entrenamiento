package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.addCalorias

import CaloriasViewModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentAgregaCaloriasBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregaCalorias.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgregaCalorias : Fragment() {

    private var _binding:FragmentAgregaCaloriasBinding? = null

    private val args:AgregaCaloriasArgs by navArgs()
    private lateinit var  viewModel:CaloriasViewModel
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = CaloriasViewModelFactory(args.idUser)
        viewModel = ViewModelProvider(this, factory).get(CaloriasViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentAgregaCaloriasBinding.inflate(inflater,container,false)
        val root:View = binding.root

        viewModel.calorias.observe(viewLifecycleOwner, Observer {
            binding.textView6.text = it.toString()
        })

        binding.buttonCalorias.setOnClickListener {
            val caloriasText = binding.editTextNumber.text.toString()

            // Validamos que no esté vacío y sea un número
            val caloriasInt = caloriasText.toIntOrNull()
            if (caloriasInt == null) {
                Toast.makeText(context, "Ingresa un número válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val idUserInt = args.idUser // Si args.idUser ya es Int, no hace falta convertir

            viewModel.insertOrUpdateCalorias(
                idUserInt,
                caloriasInt
            ) { result ->
                if (result) {
                    Toast.makeText(context,"Datos subidos", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context,"Fallo al subir datos", Toast.LENGTH_LONG).show()
                }
            }
        }


        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregaCalorias.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregaCalorias().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class CaloriasViewModelFactory(
    private val idUser: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CaloriasViewModel(idUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
