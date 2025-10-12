package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import android.util.Log
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ActivityMainBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DataSesion
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.PreferencesData
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name ="USER_PREFERENCES_NAME")
lateinit var User:UserData
val urlServidor = "http://192.168.166.33/phpdocs/"
val urlServidorImagenes = "https://supabase.com/dashboard/project/agguxkrncdvkchsetlwq/storage/buckets/img"
var idUser:String? = null

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView:BottomNavigationView

    lateinit var supabase: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "Antes de installSplashScreen")
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate ejecutado, URL Servidor: $urlServidor")
        //Pantalla de carga splash screen
        Thread.sleep(500)
        screenSplash.setKeepOnScreenCondition{false}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SupabaseInstance.client = createSupabaseClient(
            supabaseUrl = "https://agguxkrncdvkchsetlwq.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFnZ3V4a3JuY2R2a2Noc2V0bHdxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTczNDE2MjIsImV4cCI6MjA3MjkxNzYyMn0.7iLbx1dcHHwm7IA5uEuYEy5-7H9JVw4sPrSWPmuCSuk"
        ){
            install(Auth)
            install(io.github.jan.supabase.postgrest.Postgrest)
        }
        Log.d("Supabase","Supabase Iniciado")

        //Test de supabase
        lifecycleScope.launch {
            try {
                // Esto obtiene todos los registros de la tabla "user" y los mapea a la clase User
                val response = supabase.postgrest.from("user").select().decodeList<User1>()

                Log.d("SupabaseTest", "Registros obtenidos: $response")

                if (response.isNotEmpty()) {
                    val firstUser = response[0]
                    Log.d("SupabaseTest", "Primer usuario: $firstUser")
                } else {
                    Log.d("SupabaseTest", "No hay registros en la tabla 'user'")
                }
            } catch (e: Exception) {
                Log.e("SupabaseTest", "Error al consultar Supabase: ${e.message}", e)
            }
        }

        lifecycleScope.launch {
            try {
                val email = "bubu_test@gmail.com"
                val password = "abc123"

                // --- REGISTRO ---
                val signUpResult = supabase.auth.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                    this.email = email
                    this.password = password
                }

                if (signUpResult != null) {
                    Log.d("SupabaseTest", "Usuario registrado con éxito: $signUpResult")
                } else {
                    Log.d("SupabaseTest", "Registro completado, revisar confirmación de email si aplica")
                }

                // --- LOGIN ---
                val signInResult = supabase.auth.signInWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                    this.email = email
                    this.password = password
                }

                Log.d("SupabaseTest", "Login exitoso: ${supabase.auth.currentUserOrNull()}")

            } catch (e: Exception) {
                Log.e("SupabaseTest", "Error durante prueba auth: ${e.message}", e)
            }
        }
        //Test de supabase

        actionBar?.setDisplayShowTitleEnabled(true);
        //actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        navView = binding.navView

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }

    //Muestra los botones de la barra de navegación
    fun showBottomNav() {
        if (::navView.isInitialized) navView.visibility = View.VISIBLE
    }
    //Oculta los botones de la barra de navegación
    fun hideBottomNav() {
        if (::navView.isInitialized) navView.visibility = View.GONE
    }
    //Guarda datos que sean conjuntos de datos pequeños y simples, como el almacenamiento de datos de inicio de sesión, la configuración del modo oscuro, el tamaño de la fuente, entre otros
    suspend fun saveValues(correo:String, loginSucces:Boolean, metodoLogueo:String, idUser:String){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("correo")] = correo
            preferences[stringPreferencesKey("metodoLogue")] = metodoLogueo
            preferences[booleanPreferencesKey("loginSucces")]= loginSucces
            preferences[stringPreferencesKey("idUser")]= idUser
        }
    }
    suspend fun deleteValues() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("correo"))
            preferences.remove(stringPreferencesKey("metodoLogue"))
            preferences.remove(booleanPreferencesKey("loginSucces"))
            preferences.remove(stringPreferencesKey("idUser"))
        }
    }

    suspend fun saveValuesPreferences(voz:String, estado:Boolean){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("voz")] = voz
            preferences[booleanPreferencesKey("habilitarvoz")] = estado
        }
    }
    suspend fun saveValuesPreferencesVoz(voz:String){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("voz")] = voz
        }
    }
    suspend fun saveValuesPreferencesEstado(estado:Boolean){
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("habilitarvoz")] = estado
        }
    }
    suspend fun saveValueIdUser(idUser:String){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("idUser")]= idUser
        }
    }

    //Permite acceder a los diferentes datos almacenados en el dataStore
    fun getUserProfile()= dataStore.data.map { preferences ->
        DataSesion(
            correo = preferences[stringPreferencesKey("correo")].orEmpty(),
            metodoLogue = preferences[stringPreferencesKey("metodoLogue")].orEmpty(),
            loginSucces = preferences[booleanPreferencesKey("loginSucces")]?:false,
            idUser = preferences[stringPreferencesKey("idUser")].orEmpty()
        )
    }

    fun getPreferences()=dataStore.data.map {
        PreferencesData(
            voz =it[stringPreferencesKey("voz")].orEmpty(),
            habilitarVoz = it[booleanPreferencesKey("habilitarvoz")]?:false
        )
    }
    companion object{
        fun getURL():String{
            return urlServidor
        }
        fun geURLimage():String{
            return urlServidorImagenes
        }

        // Metodo para obtener el valor almacenado de loginSucces
        suspend fun getLoginSucces(context: Context): Boolean {
            val dataStore = context.dataStore
            val loginSuccesKey = booleanPreferencesKey("loginSucces")
            val preferences = dataStore.data.first()

            return preferences[loginSuccesKey] ?: false // Devuelve false si no existe el valor
        }

        // Metodo para obtener el valor almacenado de idUser
        suspend fun getIdUser(context: Context): String? {
            val dataStore = context.dataStore
            val idUserKey = stringPreferencesKey("idUser")
            val preferences = dataStore.data.first()

            return preferences[idUserKey]
        }
    }

}


