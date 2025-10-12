package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.configuracion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentPreferenciasDeVozBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces.VozHombre
import com.example.sistemaentrenamientocorporalypreparacinfisica.estrategiasVoces.VozMujer
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DataSesion
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.PreferencesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class preferenciasDeVoz : Fragment() {

    private var _binding: FragmentPreferenciasDeVozBinding? = null
    private lateinit var preferencesData: PreferencesData
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Datos de la sesion
         */
        val mainActivity = requireActivity() as MainActivity
        lifecycleScope.launch(Dispatchers.IO){
            mainActivity.getPreferences().collect(){
                preferencesData = PreferencesData(it.voz,it.habilitarVoz)
            }
        }
        //binding.switch1.isChecked = preferencesData.habilitarVoz
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreferenciasDeVozBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.switch1.isChecked = preferencesData.habilitarVoz

        if(preferencesData.habilitarVoz == false){
            binding.radioGroup
        }
        if(preferencesData.voz == "Hombre"){
            binding.radioHombre.isChecked = true
        }else{
            binding.radioMujer.isChecked = true
        }

        val mainActivity = requireActivity() as MainActivity

        binding.switch1.setOnCheckedChangeListener(){buttonView,isChecked->
            if (isChecked) {
                lifecycleScope.launch(Dispatchers.IO){
                    withContext(Dispatchers.Main){
                        mainActivity?.saveValuesPreferencesEstado(true)
                    }
                }
            } else {
                lifecycleScope.launch(Dispatchers.IO){
                    withContext(Dispatchers.Main){
                        mainActivity?.saveValuesPreferencesEstado(false)
                    }
                }
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioHombre-> {
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            mainActivity?.saveValuesPreferencesVoz("Hombre")
                        }
                    }
                }
                R.id.radioMujer -> {
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            mainActivity?.saveValuesPreferencesVoz("Mujer")
                        }
                    }
                }
            }
        }

        return root
    }
}