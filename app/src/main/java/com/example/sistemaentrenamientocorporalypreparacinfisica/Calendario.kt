package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Calendario {
    val calendar:Calendar
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    init {
        calendar = Calendar.getInstance()
    }
    fun fechaHoy():String{
        return dateFormat.format(Calendar.getInstance().time)
    }
    fun getDate():String{
        return dateFormat.format(calendar.getTime())
    }

    //Devuelve el dia de la semana
    fun diaSemana():Int{
        return calendar.get(Calendar.DAY_OF_WEEK)
    }
    //Devuelve el dia del mes
    fun diaMes():Int{
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
    //Aumenta un solo dia en el calendario
    fun aumentaDia(){
        calendar.add(Calendar.DAY_OF_YEAR, 1);
    }
    //Quita un solo dia en el calendario
    fun quitaDia(){
        calendar.add(Calendar.DAY_OF_YEAR, -1);
    }
    //Solo convierte a cadena el resultado
    fun obtenerNombreDia(diaDeLaSemana: Int): String? {
        return when (diaDeLaSemana) {
            Calendar.SUNDAY -> "Domingo"
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            else -> ""
        }
    }
    fun formatDate(inputDate: String): String {
        // Formato de entrada
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        // Formato de salida
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        // Parsear la fecha de entrada
        val date: Date = inputFormat.parse(inputDate) ?: return ""

        // Formatear la fecha al formato deseado
        return outputFormat.format(date)
    }
}