package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.DatePickerFragment
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentCuestionarioFirtsLoginBinding
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvancesInsert

class Cuestionario_firts_login : Fragment() {

    private var _binding: FragmentCuestionarioFirtsLoginBinding? = null
    private val binding get() = _binding!!
    private val args: Cuestionario_firts_loginArgs by navArgs()

    private val alturasArray = alturas()
    private val pesosArray = pesos()
    private val paisesAmerica = arrayOf(
        "Argentina","Bolivia","Brasil","Canadá","Chile","Colombia","Costa Rica","Cuba",
        "Ecuador","El Salvador","Estados Unidos","Guatemala","Honduras","México",
        "Nicaragua","Panamá","Paraguay","Perú","República Dominicana","Uruguay","Venezuela"
    )

    private var edadUsuario: Int = 0
    private var fechaNacimiento = Calendar.getInstance()
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuestionarioFirtsLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // --- Listeners y pickers ---
        binding.icon.setOnClickListener { showDatePickerDialog() }

        binding.numberPickerPeso.apply {
            maxValue = pesosArray.size - 1
            minValue = 0
            displayedValues = pesosArray
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        binding.alturaPicker.apply {
            maxValue = alturasArray.size - 1
            minValue = 0
            displayedValues = alturasArray
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        binding.paisPicker.apply {
            maxValue = paisesAmerica.size - 1
            minValue = 0
            displayedValues = paisesAmerica
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        // --- Botón enviar ---
        binding.button2.setOnClickListener {
            val radioGroup = binding.radioGroup
            val selectedOption = radioGroup.checkedRadioButtonId
            if (binding.editTextDate.text.toString().isEmpty()) {
                Toast.makeText(context, "Ingrese su fecha de nacimiento", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (selectedOption == -1) {
                Toast.makeText(context, "Ingrese su sexo", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val radioButton = radioGroup.findViewById<RadioButton>(selectedOption)
            val sexoCaracter = radioButton.text[0].toString()
            val fechaStr = binding.editTextDate.text.toString()
            val peso = pesosArray[binding.numberPickerPeso.value].toDouble()
            val altura = alturasArray[binding.alturaPicker.value].toDouble()
            val pais = paisesAmerica[binding.paisPicker.value]

            // Formato de fecha para BD
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaFormateada = dateFormat.format(convertirStringADate(fechaStr))

            // --- Corutina para insertar/update en Supabase ---
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Insert en usuarioavances
                    SupabaseInstance.client
                        .from("usuarioavances")
                        .insert(
                            UsuarioAvancesInsert(
                                idUser = args.idUser,
                                fechaDatos = fechaFormateada,
                                peso = peso,
                                altura = altura
                            )
                        )

                    // Update en user: fechaNacimiento y sexo
                    SupabaseInstance.client
                        .from("user")
                        .update(
                            mapOf(
                                "fechaNacimiento" to fechaFormateada,
                                "sexo" to sexoCaracter
                            )
                        ) {
                            filter { eq("idUser", args.idUser) }
                        }

                    withContext(Dispatchers.Main) {
                        findNavController().navigate(
                            Cuestionario_firts_loginDirections.actionCuestionarioFirtsLoginToSerieDatos2(
                                idUser = args.idUser
                            )
                        )
                    }

                } catch (e: Exception) {
                    Log.e("CuestionarioFL", "Error al guardar datos: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return root
    }

    // --- Funciones de fecha ---
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year ->
            onDateSelected(day, month, year)
            calcularEdad(day, month, year)
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun calcularEdad(day: Int, month: Int, year: Int) {
        val fechaHoy = Calendar.getInstance()
        val fechaNac = crearFecha(day, month, year)
        var edad = fechaHoy.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR)
        if (fechaHoy.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)) edad--
        edadUsuario = edad
    }

    private fun crearFecha(dia: Int, mes: Int, anio: Int): Calendar {
        val fecha = Calendar.getInstance()
        fecha.set(anio, mes - 1, dia)
        fechaNacimiento.set(anio, mes - 1, dia)
        return fecha
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.editTextDate.setText("$day-$month-$year")
    }

    private fun convertirStringADate(fechaStr: String): java.util.Date {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formato.parse(fechaStr)
    }

    companion object {
        fun alturas(): Array<String> {
            val inicio = 1.30
            val fin = 2.50
            val paso = 0.01
            val cantidadDeValores = ((fin - inicio) / paso).toInt() + 1
            return Array(cantidadDeValores) { ("%.2f".format(inicio + it * paso)) }
        }

        fun pesos(): Array<String> {
            val inicio = 30.0
            val fin = 250.0
            val paso = 0.1
            val cantidadDeValores = ((fin - inicio) / paso).toInt() + 1
            return Array(cantidadDeValores) { ("%.1f".format(inicio + it * paso)) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
