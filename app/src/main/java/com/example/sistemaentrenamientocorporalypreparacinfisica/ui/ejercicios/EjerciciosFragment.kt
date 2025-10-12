package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.ejercicios


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.charts.CircularGauge
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentDashboardBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DataSesion
import com.example.sistemaentrenamientocorporalypreparacinfisica.ParteTrabajoCuerpo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EjerciciosFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private var ejerciciosListaCuerpo: MutableList<ParteTrabajoCuerpo> = mutableListOf()

    private val binding get() = _binding!!
    private val indicadores = listOf("Peso", "Altura", "BMI", "Peso de grasa", "Peso muscular")
    private val valoresPrueba = arrayOf(1, 2, 3, 4, 5, 100)
    private var dataSesion: DataSesion? = null

    override fun onPause() {
        super.onPause()
        ejerciciosListaCuerpo.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        lifecycleScope.launch(Dispatchers.IO) {
            mainActivity.getUserProfile().collect {
                dataSesion = DataSesion(it.correo, it.metodoLogue, it.loginSucces, it.idUser)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tarjetaRutinaHoy.setOnClickListener {
            val idUserInt = dataSesion?.idUser?.toIntOrNull() ?: -1
            if (idUserInt != -1) {
                findNavController().navigate(
                    EjerciciosFragmentDirections.actionNavigationDashboardToEjercicioActividadSemana(
                        idUser = idUserInt
                    )
                )
            } else {
                Log.e("EjerciciosFragment", "ID de usuario inválido: ${dataSesion?.idUser}")
                Toast.makeText(requireContext(), "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.todosLosEjerciciosCard.setOnClickListener {
            val idUserInt = dataSesion?.idUser?.toIntOrNull() ?: -1
            if (idUserInt != -1) {
                findNavController().navigate(
                    EjerciciosFragmentDirections.actionNavigationDashboardToTodosLosEjercicio(
                        idUser = idUserInt
                    )
                )
            } else {
                Log.e("EjerciciosFragment", "ID de usuario inválido: ${dataSesion?.idUser}")
                Toast.makeText(requireContext(), "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.categoriasEjercicio.setOnClickListener {
            val idUserInt = dataSesion?.idUser?.toIntOrNull() ?: -1
            if (idUserInt != -1) {
                findNavController().navigate(
                    EjerciciosFragmentDirections.actionNavigationDashboardToEjercicioCategorias(
                        idUser = idUserInt
                    )
                )
            } else {
                Log.e("EjerciciosFragment", "ID de usuario inválido: ${dataSesion?.idUser}")
                Toast.makeText(requireContext(), "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            }
        }

        //Accede incluso si no hay conexion al servidor


        //

        return root
    }

    private fun crearTabla(): CircularGauge {
        val circularGauge: CircularGauge = AnyChart.circular()
        circularGauge.title("Resultados")
        return circularGauge
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}