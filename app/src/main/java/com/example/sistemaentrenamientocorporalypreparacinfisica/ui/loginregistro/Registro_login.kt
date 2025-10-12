package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.loginregistro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentRegistroLoginBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.User1
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Registro_login : Fragment() {

    private var _binding: FragmentRegistroLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepository: SupabaseAuthRepository
    private val TAG = "REGISTRO_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepository = SupabaseAuthRepository(SupabaseInstance.client)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val calendar = Calendar.getInstance()

        binding.button.setOnClickListener {
            val correo = binding.correoE.text.toString().trim()
            val nombre = binding.nombreE.text.toString().trim()
            val apellido = binding.apellidoE.text.toString().trim()
            val telefono = binding.telefonoE.text.toString().trim()
            val pass = binding.passE.text.toString().trim()
            val passCE = binding.passcE.text.toString().trim()

            // Validaciones
            when {
                correo.isEmpty() -> Toast.makeText(context, "Verifique su correo.", Toast.LENGTH_LONG).show()
                nombre.isEmpty() || !nombre.all { it.isLetter() } -> Toast.makeText(context, "Verifique su nombre.", Toast.LENGTH_LONG).show()
                apellido.isEmpty() || !apellido.all { it.isLetter() } -> Toast.makeText(context, "Verifique su apellido.", Toast.LENGTH_LONG).show()
                telefono.isEmpty() || !telefono.all { it.isDigit() } -> Toast.makeText(context, "Verifique su telefono.", Toast.LENGTH_LONG).show()
                pass.isEmpty() || passCE.isEmpty() -> Toast.makeText(context, "Ingrese una contraseña.", Toast.LENGTH_LONG).show()
                pass != passCE -> Toast.makeText(context, "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show()
                else -> {
                    Log.d(TAG, "Intentando registro Supabase con email: $correo")
                    authRepository.registro(correo, pass) { success, errorMessage ->
                        requireActivity().runOnUiThread {
                            if (success) {
                                Log.d(TAG, "Registro Supabase Auth exitoso")
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val fecha = dateFormat.format(calendar.time)

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        // Crear usuario con la data class
                                        val newUser = User1(
                                            correo = correo,
                                            metodoLogue = "Correo",
                                            loginSucces = "1",
                                            nombre = nombre,
                                            apellido = apellido,
                                            fechaNacimiento = fecha,
                                            telefono = telefono.toLong(),
                                            sexo = "M",
                                            experenciaPrevia = "S"
                                        )

                                        // Insertar en Supabase
                                        SupabaseInstance.client
                                            .from("user")
                                            .insert(newUser)

                                        // Obtener idUser
                                        val idUserResponse = SupabaseInstance.client
                                            .from("user")
                                            .select()
                                            .decodeList<User1>()
                                            .firstOrNull { it.correo == correo }

                                        val idUser = idUserResponse?.idUser?.toString() ?: "-1"
                                        Log.d(TAG, "ID usuario obtenido: $idUser")

                                        requireActivity().runOnUiThread {
                                            findNavController().navigate(
                                                Registro_loginDirections.actionRegistroLoginToScreenBienvenido2(
                                                    correo = correo,
                                                    realizarCuestionario = true,
                                                    metodologueo = "Correo",
                                                    idUser = idUser
                                                )
                                            )
                                        }
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error al insertar/obtener usuario en Supabase: ${e.message}", e)
                                        requireActivity().runOnUiThread {
                                            Toast.makeText(context, "Error al registrar datos: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error registro Supabase Auth: $errorMessage")
                                Toast.makeText(context, errorMessage ?: "Error al registrar usuario", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
