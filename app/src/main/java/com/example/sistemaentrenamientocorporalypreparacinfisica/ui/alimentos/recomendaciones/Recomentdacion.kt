package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.recomendaciones

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
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentRecomentdacionBinding
import com.squareup.picasso.Picasso
class Recomentdacion : Fragment() {

    private var _binding: FragmentRecomentdacionBinding? = null
    private val binding get() = _binding!!
    private val args:RecomentdacionArgs by navArgs()
    private lateinit var viewModel:AlimentoViewModel
    private lateinit var ruta:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val factory = AlimentosViewModelFactory(args.idAlimento.toString(),requireContext())
        viewModel = ViewModelProvider(this, factory).get(AlimentoViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecomentdacionBinding.inflate(inflater,container,false)
        val root:View = binding.root
        viewModel.alimento.observe(viewLifecycleOwner, Observer { alimento ->
            binding.name.text = alimento.nombre
            binding.formaPreparacion.text = alimento.descripcion
            // Picasso usa la URL completa que viene de la base de datos
            Picasso.get().load(alimento.imagen).into(binding.imagen)
        })

        binding.button3.setOnClickListener(){
            findNavController().popBackStack()
        }

        return root
    }
}
class AlimentosViewModelFactory(
    private val ruta:String,
    private val context: Context
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlimentoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlimentoViewModel(ruta,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}