package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.loginregistro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentScreenBienvenidoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Screen_bienvenido : Fragment() {

    private var _binding:FragmentScreenBienvenidoBinding? = null
    private val binding get() = _binding!!
    val args:Screen_bienvenidoArgs by navArgs()
    var usuarioNuevo:Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainActivity = requireActivity() as MainActivity

        //Guarda los datos principales de la sesión
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                mainActivity.saveValues(args.correo,true,args.metodologueo,args.idUser)
                mainActivity.saveValuesPreferences("Mujer",true)
                Log.d("CALOGIN", "El valor obtenido es" + args.idUser)
            }
        }

        // Inflate the layout for this fragment
        _binding = FragmentScreenBienvenidoBinding.inflate(inflater,container,false)
        val root:View = binding.root


        Log.d("CALOGIN", args.correo)

        /*
        val correo = args.correo
        var idUser:Int = 0

        val requestQueue= Volley.newRequestQueue(context)
        val url = MainActivity.getURL() + "consultagetiduser.php"

        val stringRequest = object: StringRequest(Request.Method.POST,url,
            Response.Listener{ response ->
                Log.d("CALOGIN", response)
                val jsonArray = JSONArray(response)
                for(i in 0 until jsonArray.length()){
                    val jsonObject = JSONObject(jsonArray.getString(i))
                    idUser = jsonObject.get("idUser").toString().toInt()
                    Log.d("CALOGIN", jsonObject.get("idUser").toString().toInt().toString() + "Se obtuvo el valor anterior")
                }
            }, Response.ErrorListener { error->
                Log.d("CALOGIN", "No se logro conectar")
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params.put("correo",correo)
                return params
            }
        }
        requestQueue.add(stringRequest)
         */

        binding.frame.setOnClickListener(){
            //args.realizarCuestionario
            if (args.realizarCuestionario){
                findNavController().navigate(
                    Screen_bienvenidoDirections.actionScreenBienvenidoToCuestionarioFirtsLogin2(
                        idUser = args.idUser.toInt()
                    )
                )
            }else{
                mainActivity.showBottomNav()
                findNavController().navigate(
                    Screen_bienvenidoDirections.actionScreenBienvenidoToNavigationHome(
                        idUser = args.idUser.toInt()
                    )
                )
            }
        }

        return root
    }
}