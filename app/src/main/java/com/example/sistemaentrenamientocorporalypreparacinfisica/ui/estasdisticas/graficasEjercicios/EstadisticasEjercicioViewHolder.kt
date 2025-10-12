package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas.graficasEjercicios

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.charts.Pie
import com.example.sistemaentrenamientocorporalypreparacinfisica.Calendario
import com.example.sistemaentrenamientocorporalypreparacinfisica.R
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ItemsTasblasPorEjercicioBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.EjercicioRealizadoLista
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.withContext

class EstadisticasEjercicioViewHolder(view: View): RecyclerView.ViewHolder(view){
    //val binding = ItemsCardRutinasBinding.bind(view)
    val binding = ItemsTasblasPorEjercicioBinding.bind(view)


    fun render(ejercicioRealizadoLista:EjercicioRealizadoLista){
        try {
            val anychart: AnyChartView = binding.chart
            val chart: Cartesian = AnyChart.column()

            val calendario = Calendario()
            val datos = ejercicioRealizadoLista.ejercicioRealizado

            // Verificar si datos no es nulo antes de usarlo
            datos?.let { ejercicios ->
                val data: MutableList<DataEntry> = mutableListOf()

                for (i in ejercicios) {
                    // Verificar si i.fechaEjercicio no es nulo antes de usarlo
                    if (i.fechaEjercicio != null) {
                        data.add(ValueDataEntry(calendario.formatDate(i.fechaEjercicio.toString()), i.repeticiones))
                    }
                }
                val column = chart.column(data)
                column.fill("#2196F3")
                column.stroke("#2196F3")
                chart.title(ejercicioRealizadoLista.nombre)
                anychart.setChart(chart)
            }
        } catch (e: Exception) {
            // Manejar la excepción aquí, si es necesario
            Log.e("TAG", "Error al renderizar el gráfico", e)
        }

    }


    /*
        Posible nueva tabla
        val anychart:AnyChartView = binding.chart
        val chart:Cartesian = AnyChart.column()

        val datos = ejercicioRealizadoLista.ejercioRealizado
        val data: MutableList<DataEntry> = mutableListOf()

        for(i in datos){
            data.add(ValueDataEntry(i.fechaEjercicio.toString(),i.repeticiones))
        }
        chart.data(data)
        chart.title(ejercicioRealizadoLista.nombre)
        anychart.setChart(chart)
     */
}