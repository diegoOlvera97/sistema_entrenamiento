package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.formulario

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sistemaentrenamientocorporalypreparacinfisica.MainActivity
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.FragmentSerieDatos1Binding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Discapacidad
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlergiasPantalla.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlergiasPantalla : Fragment() {

    private var _binding: FragmentSerieDatos1Binding? = null
    private val binding get() = _binding!!
    private var rutaServidor = MainActivity.getURL()

    val args:AlergiasPantallaArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSerieDatos1Binding.inflate(inflater, container, false)
        val root:View = binding.root

        val requestQueue = Volley.newRequestQueue(context)
        val url = rutaServidor + "consulta_discapacidades.php"

        var discapacidades = mutableListOf<Discapacidad>()
        var nada = ArrayList<String>()

        val stringRequest =StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                val jsonArray = JSONArray(response)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = JSONObject(jsonArray.getString(i))
                    val discapacidad = Discapacidad(jsonObject.get("nombreDis").toString())
                    nada.add(jsonObject.get("nombreDis").toString())
                    Log.d("CALOGIN", jsonObject.get("nombreDis").toString())
                    discapacidades.add(discapacidad)
                }
            },
            Response.ErrorListener { error ->
                Log.d("CALOGIN", "No se logró conectar: $error")
            })

        requestQueue.add(stringRequest)

        val spinner = binding.spinner
        if(spinner != null){
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                nada)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        return root
    }

}