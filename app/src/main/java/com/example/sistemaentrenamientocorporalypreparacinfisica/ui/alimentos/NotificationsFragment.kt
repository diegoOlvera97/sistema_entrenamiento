package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.alimentos

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentNotificationsBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Alimentos
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ColacionModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import com.example.sistemaentrenamientocorporalypreparacinfisica.User1
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvances
import java.util.Locale

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationsViewModel
    private var ruta: String = MainActivity.geURLimage()
    val calendar: Calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    var idUserV: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            idUserV = MainActivity.getIdUser(requireContext())
            idUserV?.let {
                val factory = AlimentosViewModelFactory(requireContext(), it)
                viewModel = ViewModelProvider(this@NotificationsFragment, factory).get(NotificationsViewModel::class.java)
                observeViewModel()
            }
        }
    }



    fun harrisBenedictEquations(peso: Double, altura: Double, edad: Int, sexo: String): Double {
        return if (sexo.equals("M", ignoreCase = true) || sexo.equals("Masculino", ignoreCase = true)) {
            // Fórmula para hombres
            66.47 + (13.75 * peso) + (5.0 * altura) - (6.75 * edad)
        } else {
            // Fórmula para mujeres
            655.1 + (9.56 * peso) + (1.85 * altura) - (4.68 * edad)
        }
    }


    private fun observeViewModel() {
        var currentUser: User1? = null
        var currentAvance: UsuarioAvances? = null

        // Observa los datos de usuario
        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            currentUser = user
            // recalcular si ya tenemos avance
            currentAvance?.let { avance ->
                actualizarMeta(user, avance)
            }
        })

        // Observa los datos de avances
        viewModel.usuarioAvancesData.observe(viewLifecycleOwner, Observer { avance ->
            currentAvance = avance
            // recalcular si ya tenemos usuario
            currentUser?.let { user ->
                actualizarMeta(user, avance)
            }
        })
    }

    private fun actualizarMeta(user: User1, avance: UsuarioAvances) {
        binding.metaValor.text = harrisBenedictEquations(
            avance.peso,
            avance.altura,
            obtenerEdad(user.fechaNacimiento),
            user.sexo// 👈 convierto String a Char
        ).toString()
    }


    fun obtenerEdad(fechaNacimiento: String): Int {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fecha: Date = formato.parse(fechaNacimiento) ?: return 0

        val nacimiento = java.util.Calendar.getInstance().apply { time = fecha }
        val hoy = java.util.Calendar.getInstance()

        var edad = hoy.get(java.util.Calendar.YEAR) - nacimiento.get(java.util.Calendar.YEAR)

        if (hoy.get(java.util.Calendar.DAY_OF_YEAR) < nacimiento.get(java.util.Calendar.DAY_OF_YEAR)) {
            edad--
        }

        return edad
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        viewModel.colaciones.observe(viewLifecycleOwner, Observer {colacionList->
            viewModel.alimentos.observe(viewLifecycleOwner, Observer {alimentos ->
                //initRecyclerView(alimentos, colacionList)
            })
        })
         */

        binding.comidasRecomendadas.setOnClickListener {
            findNavController().navigate(
                NotificationsFragmentDirections.actionNavigationNotificationsToTipoComida()
            )
        }
        binding.colacionesRecomendadas.setOnClickListener {
            findNavController().navigate(
                NotificationsFragmentDirections.actionNavigationNotificationsToTipoColacion()
            )
        }
        binding.botonCalorias.setOnClickListener {
            findNavController().navigate(
                NotificationsFragmentDirections.actionNavigationNotificationsToAgregaCalorias(
                    idUser = idUserV!!.toInt()
                )
            )
        }
        return root
    }

    private fun initRecyclerView(listaAlimentos: List<Alimentos>, colacion: List<ColacionModel>) {
        //binding.recyclerAlimentos.layoutManager = LinearLayoutManager(context)
        //binding.recyclerAlimentos.adapter = AlimentosAdapter(listaAlimentos,colacion,ruta,{onItemSelected(it)},{onButomSelected(it)})
    }

    private fun onItemSelected(it: Alimentos) {
    }

    private fun onButomSelected(it: ColacionModel) {
    }

    private fun fechaHoy(): String {
        return dateFormat.format(Calendar.getInstance().time)
    }

    private fun getDate(): String {
        return dateFormat.format(calendar.getTime())
    }

    private fun diaSemana(): Int {
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun diaMes(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun aumentaDia() {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    private fun quitaDia() {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
    }

    private fun obtenerNombreDia(diaDeLaSemana: Int): String? {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class AlimentosViewModelFactory(
    private val context: Context,
    private val idUser: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(context, idUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}