package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas.graficasEjercicios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentEstadisticasEjercicioBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizadoLista

class EstadisticasEjercicio : Fragment() {

    private var _binding: FragmentEstadisticasEjercicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EstadisticasEjercicioViewModel
    private val args: EstadisticasEjercicioArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = EstadisticasEjerciciosViewModelFactory(args.idUser, requireContext())
        viewModel = ViewModelProvider(this, factory).get(EstadisticasEjercicioViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstadisticasEjercicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OBSERVE: Tiempo por ejercicio
        viewModel.tiempoEjercicios.observe(viewLifecycleOwner) { lista ->
            val listaFiltrada = lista.filter { it.tiempo > 0 }
            val pieChar: Pie = AnyChart.pie()
            val anyChartView: AnyChartView = binding.chartPie

            if (listaFiltrada.isNotEmpty()) {
                val data: MutableList<DataEntry> = mutableListOf()
                var totalTiempo = 0
                listaFiltrada.forEach {
                    data.add(ValueDataEntry(it.nombre, it.tiempo))
                    totalTiempo += it.tiempo.toInt()
                }
                pieChar.data(data)
                pieChar.title().useHtml(true)
                pieChar.title("<strong>Tiempo invertido: ${segundosAMinutos(totalTiempo)} en la última semana</strong>")
            } else {
                pieChar.title("SIN DATOS AUN")
            }

            anyChartView.setChart(pieChar)
        }

        // OBSERVE: Repeticiones por ejercicio
        viewModel.datosEjerciciosRealizados.observe(viewLifecycleOwner) { lista ->
            // Filtrar y limpiar cada lista interna
            val listaFiltrada = lista.mapNotNull { ejercicioLista ->
                val realizadosConReps = ejercicioLista.ejercicioRealizado.orEmpty().filter { it.repeticiones > 0 }
                if (realizadosConReps.isNotEmpty()) {
                    // Crear un nuevo objeto con solo los que tienen repeticiones
                    EjercicioRealizadoLista(
                        nombre = ejercicioLista.nombre,
                        ejercicioRealizado = realizadosConReps.toMutableList()
                    )
                } else {
                    null // Si no hay repeticiones, no se incluye
                }
            }

            if (listaFiltrada.isNotEmpty()) {
                binding.recyclerEstadisticasEjercicio.layoutManager = LinearLayoutManager(context)
                binding.recyclerEstadisticasEjercicio.adapter = EstadisticasEjercicioAdapter(listaFiltrada)
                binding.recyclerEstadisticasEjercicio.visibility = View.VISIBLE
            } else {
                binding.recyclerEstadisticasEjercicio.visibility = View.GONE
            }
        }

    }

    private fun segundosAMinutos(segundos: Int): String {
        val horas = segundos / 3600
        val minutos = (segundos % 3600) / 60
        val segundosRestantes = segundos % 60
        return if (horas > 0) String.format("%d:%02d:%02d", horas, minutos, segundosRestantes)
        else String.format("%d:%02d", minutos, segundosRestantes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
