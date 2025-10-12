package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.configuracion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentAcercaDeBinding

class AcercaDe : Fragment() {

    private  var _binding: FragmentAcercaDeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAcercaDeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textoAcercaDe = """
            |¡Bienvenido a nuestra aplicación!
            |
            |Esta aplicación ha sido diseñada para ayudarte a llevar un control de tu actividad fisica.
            |
            |Somos una empresa con sede en Ecatepec de Morelos, México, comprometida con ofrecer soluciones innovadoras a nuestros usuarios.
            |
            |Características principales:
            |- Implementa la clasificacion de movimiento por medio de ML Kit
            |- Recomienda ejercicios en funcion de lo realizado por todos los usuarios en funcion de una RNN creada en TF 
            |- Obtención de datos de basculas particulares
            |
            |
            |Gracias por usar nuestra aplicación y por tu apoyo continuo.
        """.trimMargin()

        binding.textoAcerca.text = textoAcercaDe

        return root
    }
}