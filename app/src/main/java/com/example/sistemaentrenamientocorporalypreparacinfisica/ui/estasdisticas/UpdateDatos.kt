package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentUpdateDatosBinding

class UpdateDatos : Fragment() {
    private var _binding: FragmentUpdateDatosBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateDatosArgs by navArgs()
    private lateinit var viewModel: UpdateDatosViewModel
    private lateinit var estadisticasViewModel: EstadisticasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = UpdateDatosViewModelFactory(args.idUser.toString(), requireContext())
        viewModel = ViewModelProvider(this, factory).get(UpdateDatosViewModel::class.java)

        // Obtener EstadisticasViewModel (compartido si quieres que se actualice)
        estadisticasViewModel = ViewModelProvider(requireActivity(), EstadisticasViewModelFactory(args.idUser.toString(), requireContext()))
            .get(EstadisticasViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.usuarioAvances.observe(viewLifecycleOwner) { it ->
            it?.let {
                binding.entradaPeso.text = Editable.Factory.getInstance().newEditable(it.peso.toString())
                binding.entradaAltura.text = Editable.Factory.getInstance().newEditable(it.altura.toString())
                binding.entradagrasa.text = Editable.Factory.getInstance().newEditable((it.pesoGrasa ?: 0.0).toString())
                binding.entradaMusculo.text = Editable.Factory.getInstance().newEditable((it.pesoMusculo ?: 0.0).toString())
            }
        }

        // Observamos cambios para refrescar EstadisticasViewModel
        viewModel.datosActualizados.observe(viewLifecycleOwner) { actualizado ->
            if (actualizado) {
                estadisticasViewModel.updateData()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateDatosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.guardarDatos.setOnClickListener {
            saveData()
        }
        return root
    }

    private fun validarNumero(numero: String): Boolean {
        return numero.toDoubleOrNull() != null
    }

    private fun saveData() {
        val usuario = viewModel.usuarioAvances.value ?: return

        val peso = binding.entradaPeso.text.toString()
        val altura = binding.entradaAltura.text.toString()
        val grasa = binding.entradagrasa.text.toString()
        val musculo = binding.entradaMusculo.text.toString()

        if (!validarNumero(peso) || !validarNumero(altura) || !validarNumero(grasa) || !validarNumero(musculo)) {
            Toast.makeText(context, "Verifique todos los campos numéricos.", Toast.LENGTH_LONG).show()
            return
        }

        viewModel.insertDataUsuariosAvances(
            idUsuarioAvances = usuario.idUserAvances,
            peso = peso.toDouble(),
            altura = altura.toDouble(),
            pesoGrasa = grasa.toDoubleOrNull(),
            pesoMusculo = musculo.toDoubleOrNull()
        ) { result ->
            if (result) {
                Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Fallo al actualizar los datos", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
