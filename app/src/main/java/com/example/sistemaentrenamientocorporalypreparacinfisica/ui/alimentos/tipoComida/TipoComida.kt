package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos.tipoComida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentTipoComidaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TipoComida.newInstance] factory method to
 * create an instance of this fragment.
 */
class TipoComida : Fragment() {


    private var _binding: FragmentTipoComidaBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTipoComidaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.comidasRecomendadasDesayuno.setOnClickListener(){
            findNavController().navigate(
                TipoComidaDirections.actionTipoComidaToTipoComidaLista(
                    tipoComida = "desayuno"
                )
            )
        }
        binding.comidasRecomendadasComida.setOnClickListener(){
            findNavController().navigate(
                TipoComidaDirections.actionTipoComidaToTipoComidaLista(
                    tipoComida = "comida"
                )
            )
        }
        binding.comidasRecomendadasCena.setOnClickListener(){
            findNavController().navigate(
                TipoComidaDirections.actionTipoComidaToTipoComidaLista(
                    tipoComida = "cena"
                )
            )
        }
        return root
    }
}

