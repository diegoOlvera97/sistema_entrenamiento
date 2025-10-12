package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios.rutinas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.PreferencesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class PermissionsFragment : Fragment() {

    private val args:PermissionsFragmentArgs by navArgs()
    private lateinit var preferencesData: PreferencesData
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    context,
                    "Permission request granted",
                    Toast.LENGTH_LONG
                ).show()
                navigateToCamera()
            } else {
                Toast.makeText(
                    context,
                    "Permission request denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        lifecycleScope.launch(Dispatchers.IO){
            mainActivity.getPreferences().collect(){
                preferencesData = PreferencesData(it.voz,it.habilitarVoz)

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) -> {
                        navigateToCamera()
                    }
                    else -> {
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                }
            }
        }

    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment_activity_main
            ).navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToInciarEjercicio(
                    idUser = args.idUser,
                    nombreEjercicio = args.nombreEjercicio,
                    idEjer = args.idEjer,
                    tipoVoz = preferencesData.voz,
                    habilitarVoz = preferencesData.habilitarVoz
                )
            )
        }
    }

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

}
