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
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentAcederLoginBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.User1
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Aceder_login : Fragment() {

    private var _binding: FragmentAcederLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepository: SupabaseAuthRepository
    private val TAG: String = "ACCEDER_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepository = SupabaseAuthRepository(SupabaseInstance.client)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcederLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.button.setOnClickListener {
            val email = binding.mailE.text.toString().trim()
            val password = binding.pass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, llene todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Formato de correo inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Intentando login con email: $email, password: [HIDDEN]")

            // Llamada a SupabaseAuthRepository
            authRepository.login(email, password) { success, errorMessage ->
                requireActivity().runOnUiThread {
                    if (success) {
                        Log.d(TAG, "Login exitoso con Supabase")

                        // Obtener usuario desde la tabla user
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val userResponse = SupabaseInstance.client
                                    .from("user")
                                    .select()
                                    .decodeList<User1>()
                                    .firstOrNull { it.correo == email }

                                val idUser = userResponse?.idUser?.toString() ?: "-1"
                                Log.d(TAG, "idUser: $idUser")

                                requireActivity().runOnUiThread {
                                    findNavController().navigate(
                                        Aceder_loginDirections.actionAcederLoginToScreenBienvenido2(
                                            correo = email,
                                            realizarCuestionario = false,
                                            metodologueo = "Correo",
                                            idUser = idUser
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error al obtener usuario en Supabase: ${e.message}", e)
                                requireActivity().runOnUiThread {
                                    Toast.makeText(requireContext(), "Sin conexión al servidor", Toast.LENGTH_LONG).show()
                                    findNavController().navigate(
                                        Aceder_loginDirections.actionAcederLoginToScreenBienvenido2(
                                            correo = email,
                                            realizarCuestionario = false,
                                            metodologueo = "Correo",
                                            idUser = "-1"
                                        )
                                    )
                                }
                            }
                        }

                    } else {
                        Log.e(TAG, "Error login Supabase: $errorMessage")
                        Toast.makeText(requireContext(), errorMessage ?: "Error al iniciar sesión", Toast.LENGTH_LONG).show()
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
