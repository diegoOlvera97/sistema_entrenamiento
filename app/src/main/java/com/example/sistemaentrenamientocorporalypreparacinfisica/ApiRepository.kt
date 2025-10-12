package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Alimentos
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Calorias
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ColacionModel
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.Ejercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ObjetivosEjercicio
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ObjetivosPython
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.ParteTrabajoCuerpo
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.RepeticionesPython
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.TiempoEjercicios
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.UserData
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.UsuarioAvances
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ApiRepository private constructor(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)
    private val gson = Gson()

    companion object {
        @Volatile
        private var INSTANCE: ApiRepository? = null

        fun getInstance(context: Context): ApiRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiRepository(context).also { INSTANCE = it }
            }
        }
    }

    fun getUserIdByEmail(email: String, onSuccess: (String) -> Unit, onError: (VolleyError) -> Unit) {
        val url = MainActivity.getURL() + "consultagetiduser.php"
        Log.d("ApiRepository", "Solicitando ID de usuario para email: $email, URL: $url")

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("ApiRepository", "Respuesta del servidor: $response")
                try {
                    val jsonArray = JSONArray(response)
                    var idUserQuery: String? = null
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = JSONObject(jsonArray.getString(i))
                        idUserQuery = jsonObject.getString("idUser")
                    }
                    if (idUserQuery != null && idUserQuery.isNotEmpty()) {
                        onSuccess(idUserQuery)
                    } else {
                        Log.e("ApiRepository", "No se encontró el usuario para email: $email")
                        onError(VolleyError("No se encontró el usuario"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor: ${e.message}"))
                }
            },
            Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("correo" to email)
            }
        }

        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(stringRequest)
    }

    fun insertNewUserData(
        correo: String,
        metodoLogueo: String,
        loginSucess: String,
        nombre: String,
        apellido: String,
        fechaNacimiento: String,
        telefono: String,
        sexo: String,
        experiencia: String,
        onSuccess: (Boolean) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val url = MainActivity.getURL() + "insertar_usuario.php"

        val request = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    onSuccess(true)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                params["correo"] = correo
                params["metodoLogue"] = "Correo"
                params["loginSucces"] = "1"
                params["nombre"] = nombre
                params["apellido"] = apellido
                params["fechaNacimiento"] = dateFormat.format(calendar.time)
                params["telefono"] = telefono
                params["sexo"] = "M"
                params["experienciaPrevia"] = "S"
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }

    fun procedimiento_datos_usuario_avance(
        idUser: String,
        entradaPeso: String,
        altura: String,
        pesoGrasa: String,
        entradaMusculo: String,
        onSuccess: (Boolean) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "procedimiento_datos_usuario_avance.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    onSuccess(true)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                params["peso"] = entradaPeso
                params["altura"] = altura
                params["pesoGrasa"] = pesoGrasa
                params["pesoMusculo"] = entradaMusculo
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun procedimiento_datos_calorias_avance(
        idUser: String,
        calorias: String,
        onSuccess: (Boolean) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "procedimiento_datos_calorias_avance.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    onSuccess(true)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                params["calorias"] = calorias
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_registro_masactual(
        idUserAvance: String,
        onSuccess: (String) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_registro_masactual.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var idUserAvanceResult = ""
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = JSONObject(jsonArray.getString(i))
                        idUserAvanceResult = jsonObject.getString("idUserAvances")
                    }
                    if (idUserAvanceResult.isNotEmpty()) {
                        onSuccess(idUserAvanceResult)
                    } else {
                        Log.e("ApiRepository", "No se encontró el usuario avance para idUser: $idUserAvance")
                        onError(VolleyError("No se encontró el usuario"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUserAvance
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_registro_masactual_id(
        idUserAvances: String,
        onSuccess: (UsuarioAvances) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_registro_masactual_id.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var usuarioAvances: UsuarioAvances? = null
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        usuarioAvances = gson.fromJson(jsonArray.getString(i), UsuarioAvances::class.java)
                    }
                    if (usuarioAvances != null) {
                        onSuccess(usuarioAvances)
                    } else {
                        Log.e("ApiRepository", "No se encontró el objeto UsuarioAvances para id: $idUserAvances")
                        onError(VolleyError("No se encontró el objeto UsuarioAvances"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUserAvances"] = idUserAvances
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_lista_avances_usuario(
        idUser: String,
        onSuccess: (List<UsuarioAvances>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_lista_avances_usuario.php"

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { result ->
                try {
                    val jsonArray = JSONArray(result)
                    val lista: MutableList<UsuarioAvances> = mutableListOf()
                    val gsonBuilder = GsonBuilder()
                        .registerTypeAdapter(Boolean::class.java, JsonDeserializer { json, _, _ ->
                            json.asInt == 1
                        })
                        .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                            try {
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(json.asString)
                            } catch (e: Exception) {
                                null
                            }
                        })
                        .create()
                    for (i in 0 until jsonArray.length()) {
                        lista.add(gsonBuilder.fromJson(jsonArray.getString(i), UsuarioAvances::class.java))
                    }
                    onSuccess(lista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_tiempo_destinado(
        idUser: String,
        onSuccess: (List<TiempoEjercicios>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_tiempo_destinado.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { result ->
                try {
                    val tiempoEjerciciosLista: MutableList<TiempoEjercicios> = ArrayList()
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val tiempoEjercicio = gson.fromJson(jsonArray.getString(i), TiempoEjercicios::class.java)
                        tiempoEjerciciosLista.add(tiempoEjercicio)
                    }
                    onSuccess(tiempoEjerciciosLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_get_alimentos(
        tipoComida: String,
        onSuccess: (List<Alimentos>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_get_alimentos.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val alimentosLista: MutableList<Alimentos> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        alimentosLista.add(gson.fromJson(jsonArray.getString(i), Alimentos::class.java))
                    }
                    onSuccess(alimentosLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["tipoComida"] = tipoComida
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_get_colacion(
        tipoColacion: String,
        onSuccess: (List<ColacionModel>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_get_colacion.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val colacionLista: MutableList<ColacionModel> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        colacionLista.add(gson.fromJson(jsonArray.getString(i), ColacionModel::class.java))
                    }
                    onSuccess(colacionLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["tipoColacion"] = tipoColacion
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_colacion(
        idColacion: String,
        onSuccess: (ColacionModel) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_colacion.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var colacion: ColacionModel? = null
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        colacion = gson.fromJson(jsonArray.getString(i), ColacionModel::class.java)
                    }
                    if (colacion != null) {
                        onSuccess(colacion)
                    } else {
                        Log.e("ApiRepository", "No se encontró el objeto Colacion para id: $idColacion")
                        onError(VolleyError("No se encontró el objeto Colacion"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idColacion"] = idColacion
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_alimento(
        idAlimento: String,
        onSuccess: (Alimentos) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_alimento.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var alimento: Alimentos? = null
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        alimento = gson.fromJson(jsonArray.getString(i), Alimentos::class.java)
                    }
                    if (alimento != null) {
                        onSuccess(alimento)
                    } else {
                        Log.e("ApiRepository", "No se encontró el objeto Alimento para id: $idAlimento")
                        onError(VolleyError("No se encontró el objeto Alimentos"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idAlimento"] = idAlimento
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_datos_user(
        idUser: String,
        onSuccess: (UserData) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_datos_user.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                Log.d("ApiRepository", "Respuesta de consulta_datos_user: $response")
                try {
                    var userData: UserData? = null
                    val jsonArray = JSONArray(response)
                    val gsonBuilder = GsonBuilder()
                        .registerTypeAdapter(Boolean::class.java, JsonDeserializer { json, _, _ ->
                            json.asInt == 1
                        })
                        .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                            try {
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(json.asString)
                            } catch (e: Exception) {
                                null
                            }
                        })
                        .registerTypeAdapter(Char::class.javaObjectType, JsonDeserializer { json, _, _ ->
                            json.asString.firstOrNull()
                        })
                        .create()
                    for (i in 0 until jsonArray.length()) {
                        userData = gsonBuilder.fromJson(jsonArray.getString(i), UserData::class.java)
                    }
                    if (userData != null) {
                        onSuccess(userData)
                    } else {
                        Log.e("ApiRepository", "No se encontró el objeto UserData para id: $idUser")
                        onError(VolleyError("No se encontró el objeto UserData"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                } catch (e: Exception) {
                    Log.e("ApiRepository", "Error desconocido: ${e.message}")
                    onError(VolleyError("Error desconocido: ${e.message}"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun procedimiento_datos_usuario_ejercicio_realizado(
        idEjercicio: String,
        idUser: String,
        repeticiones: String,
        tiempo: String,
        onSuccess: (Boolean) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "procedimiento_datos_usuario_ejercicio_realizado.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    onSuccess(true)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idEjercicio"] = idEjercicio
                params["idUser"] = idUser
                params["repeticiones"] = repeticiones
                params["tiempo"] = tiempo
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_get_ejercicios(
        onSuccess: (List<Ejercicio>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_get_ejercicios.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val ejerciciosLista: MutableList<Ejercicio> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        ejerciciosLista.add(gson.fromJson(jsonArray.getString(i), Ejercicio::class.java))
                    }
                    onSuccess(ejerciciosLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap()
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_calorias_masactual(
        idUser: String,
        onSuccess: (String) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_calorias_masactual.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var idCalorias = ""
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = JSONObject(jsonArray.getString(i))
                        idCalorias = jsonObject.getString("idCalorias")
                    }
                    if (idCalorias.isNotEmpty()) {
                        onSuccess(idCalorias)
                    } else {
                        Log.e("ApiRepository", "No se encontró el idCalorias para idUser: $idUser")
                        onError(VolleyError("No se encontró el usuario"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_calorias_masactual_id(
        idCalorias: String,
        onSuccess: (Calorias) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_calorias_masactual_id.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    var calorias: Calorias? = null
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        calorias = gson.fromJson(jsonArray.getString(i), Calorias::class.java)
                    }
                    if (calorias != null) {
                        onSuccess(calorias)
                    } else {
                        Log.e("ApiRepository", "No se encontró el objeto Calorias para id: $idCalorias")
                        onError(VolleyError("No se encontró el objeto Calorias"))
                    }
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idCalorias"] = idCalorias
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_lista_ejercicio_partes(
        onSuccess: (List<ParteTrabajoCuerpo>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_lista_ejercicio_partes.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val ejerciciosListaCuerpo: MutableList<ParteTrabajoCuerpo> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        ejerciciosListaCuerpo.add(gson.fromJson(jsonArray.getString(i), ParteTrabajoCuerpo::class.java))
                    }
                    onSuccess(ejerciciosListaCuerpo)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap()
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_ejercicio_por_parte(
        parteCuerpo: String,
        onSuccess: (List<Ejercicio>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_ejercicio_por_parte.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val ejerciciosLista: MutableList<Ejercicio> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        ejerciciosLista.add(gson.fromJson(jsonArray.getString(i), Ejercicio::class.java))
                    }
                    onSuccess(ejerciciosLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["parteCuerpo"] = parteCuerpo
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_calorias_lista_id(
        idUser: String,
        onSuccess: (List<Calorias>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_calorias_lista_id.php"

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { result ->
                try {
                    val jsonArray = JSONArray(result)
                    val lista: MutableList<Calorias> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        lista.add(gson.fromJson(jsonArray.getString(i), Calorias::class.java))
                    }
                    onSuccess(lista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun consulta_get_objetivos(
        onSuccess: (List<ObjetivosEjercicio>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_get_objetivos.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val objetivosLista: MutableList<ObjetivosEjercicio> = ArrayList()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        objetivosLista.add(gson.fromJson(jsonArray.getString(i), ObjetivosEjercicio::class.java))
                    }
                    onSuccess(objetivosLista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap()
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun procedimiento_objetivos_usuario(
        idUser: String,
        idObjetivos: String,
        onSuccess: (Boolean) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "procedimiento_objetivos_usuario.php"

        val request = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    onSuccess(true)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                params["idObjetivos"] = idObjetivos
                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }

    fun getModelObjetivosLista(
        idUser: String,
        onSuccess: (List<ObjetivosPython>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_objetivos_usuario.php"

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { result ->
                try {
                    val jsonArray = JSONArray(result)
                    val lista: MutableList<ObjetivosPython> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        lista.add(gson.fromJson(jsonArray.getString(i), ObjetivosPython::class.java))
                    }
                    onSuccess(lista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun getModelRepeticionesLista(
        idUser: String,
        onSuccess: (List<RepeticionesPython>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val url = MainActivity.getURL() + "consulta_repeticiones_usuario.php"

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { result ->
                try {
                    val jsonArray = JSONArray(result)
                    val lista: MutableList<RepeticionesPython> = ArrayList()
                    for (i in 0 until jsonArray.length()) {
                        lista.add(gson.fromJson(jsonArray.getString(i), RepeticionesPython::class.java))
                    }
                    onSuccess(lista)
                } catch (e: JSONException) {
                    Log.e("ApiRepository", "Error al procesar JSON: ${e.message}")
                    onError(VolleyError("Error al procesar la respuesta del servidor"))
                }
            }, Response.ErrorListener { error ->
                Log.e("ApiRepository", "Error en la solicitud: ${error.message}")
                onError(error)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["idUser"] = idUser
                return params
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }
}