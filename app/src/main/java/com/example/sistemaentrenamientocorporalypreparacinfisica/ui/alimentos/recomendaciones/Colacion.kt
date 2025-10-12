package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.recomendaciones

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
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentColacionBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion
import com.google.gson.Gson
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


private var _binding: FragmentColacionBinding? = null
private val binding get() = _binding!!
/**
 * A simple [Fragment] subclass.
 * Use the [ColacionModel.newInstance] factory method to
 * create an instance of this fragment.
 */
class Colacion : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel:ColacionViewModel
    private val args:ColacionArgs by navArgs()
    private lateinit var colacion:com.example.sistemaentrenamientocorporalypreparacinfisica.Colacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = ColacionViewModelFactory(args.idColacion.toString(),requireContext())
        viewModel = ViewModelProvider(this, factory).get(ColacionViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentColacionBinding.inflate(inflater,container,false)
        val root:View = binding.root
        val ruta = MainActivity.geURLimage()

        viewModel.colacion.observe(viewLifecycleOwner, Observer { colacion ->
            binding.name.text = colacion.nombre
            binding.formaPreparacion.text = colacion.descripcion
            // Picasso usa la URL completa que viene de la base de datos
            Picasso.get().load(colacion.imagen).into(binding.imagen)
        })


        binding.button3.setOnClickListener(){
            findNavController().popBackStack()
        }

        return root
    }
}

class ColacionViewModelFactory(
    private val ruta:String,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ColacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ColacionViewModel(ruta,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}