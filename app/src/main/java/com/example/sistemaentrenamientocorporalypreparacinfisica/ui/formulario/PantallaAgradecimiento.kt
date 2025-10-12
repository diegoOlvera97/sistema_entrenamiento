package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentAlergiasPantallaBinding

class PantallaAgradecimiento : Fragment() {

    private var _binding:FragmentAlergiasPantallaBinding? = null
    val args:PantallaAgradecimientoArgs by navArgs()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity = requireActivity() as MainActivity
        _binding = FragmentAlergiasPantallaBinding.inflate(inflater,container,false)
        val root:View = binding.root

        binding.frame.setOnClickListener(){
            mainActivity.showBottomNav()
            findNavController().navigate(
                PantallaAgradecimientoDirections.actionAlergiasPantallaToNavigationHome(
                    idUser = args.idUser
                )
            )
        }

        return root
    }

}