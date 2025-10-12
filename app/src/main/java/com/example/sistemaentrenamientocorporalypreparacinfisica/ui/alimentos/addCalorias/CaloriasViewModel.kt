import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.Calorias
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaloriasViewModel(
    private val idUser: Int
) : ViewModel() {

    private val TAG = "CaloriasViewModel"

    private val _calorias = MutableLiveData<Double>()
    val calorias: LiveData<Double> = _calorias
    // INSERT (no mandar idCalorias)
    val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


    init {
        obtenerCaloriasHoy()
    }

    private fun obtenerCaloriasHoy() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("calorias")
                    .select(Columns.ALL) {
                        filter {
                            eq("idUser", idUser)
                            eq("fechaDatos", fechaActual) // ahora sí es un String con la fecha
                        }
                        order("fechaDatos", Order.DESCENDING)
                        limit(1)
                    }
                    .decodeList<Calorias>()

                _calorias.postValue((response.firstOrNull()?.calorias ?: 0.0).toDouble())
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener calorías: ${e.message}", e)
                _calorias.postValue(0.0)
            }
        }
    }

    fun insertOrUpdateCalorias(idUser: Int, calorias: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Buscar el registro del usuario en la fecha actual
                val existente = SupabaseInstance.client.postgrest
                    .from("calorias")
                    .select(Columns.ALL) {
                        filter {
                            eq("idUser", idUser)
                            eq("fechaDatos", fechaActual) //  filtramos por fecha exacta
                        }
                        limit(1)
                    }
                    .decodeList<Calorias>()
                    .firstOrNull()

                if (existente != null && existente.idCalorias != null) {
                    // UPDATE sumando calorías
                    val nuevasCalorias = (existente.calorias ?: 0) + calorias
                    SupabaseInstance.client.postgrest
                        .from("calorias")
                        .update(
                            Calorias(
                                idCalorias = existente.idCalorias,
                                idUser = idUser,
                                fechaDatos = fechaActual,
                                calorias = nuevasCalorias
                            )
                        ) {
                            filter { eq("idCalorias", existente.idCalorias) }
                        }
                    _calorias.postValue(nuevasCalorias.toDouble()) //  actualizamos el LiveData
                    onResult(true)
                } else {
                    // INSERT   (primer registro del día)
                    SupabaseInstance.client.postgrest
                        .from("calorias")
                        .insert(
                            Calorias(
                                idCalorias = null,
                                idUser = idUser,
                                fechaDatos = fechaActual,
                                calorias = calorias
                            )
                        )
                    _calorias.postValue(calorias.toDouble()) // actualizamos el LiveData
                    onResult(true)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al insertar/actualizar calorías: ${e.message}", e)
                onResult(false)
            }
        }
    }
}