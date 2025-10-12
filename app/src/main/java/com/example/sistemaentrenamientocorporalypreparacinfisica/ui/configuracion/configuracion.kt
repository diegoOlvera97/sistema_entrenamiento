package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.configuracion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentConfiguracionBinding
import kotlinx.coroutines.launch

class configuracion : Fragment() {

    private var _binding: FragmentConfiguracionBinding? = null
    private lateinit var viewModel: ConfiguracionViewModel
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val idUser = MainActivity.getIdUser(requireContext()) ?: return@launch
            val factory = ConfiguracionViewModelFactory(idUser)
            viewModel = ViewModelProvider(this@configuracion, factory)
                .get(ConfiguracionViewModel::class.java)

            observerViewModel()
        }
    }

    private fun observerViewModel() {
        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            binding.nombreE.text = "${user.nombre} ${user.apellido}"
            binding.telefonoE.text = user.telefono.toString()
            binding.correoE.text = user.correo
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainActivity = requireActivity() as MainActivity
        _binding = FragmentConfiguracionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /**
         * Cerrar sesión
         */
        binding.buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                mainActivity.deleteValues()

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true)
                    .build()

                findNavController().navigate(R.id.firts_login, null, navOptions)
            }
        }

        binding.acercaDe.setOnClickListener {
            findNavController().navigate(
                configuracionDirections.actionNavigationSettingsToAcercaDe()
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
