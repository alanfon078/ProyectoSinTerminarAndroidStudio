package com.example.proyecto.network

import android.annotation.SuppressLint
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

//@kotlinx.serialization.Serializable
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class LoginRequest(val Username: String, val Password: String)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NuevoInvitadoRequest(
    // El JSON enviado usará estos nombres en minúscula por defecto
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    // La propiedad en C# es Id_Residente, que se convierte a id_Residente en JSON camelCase.
    // Para evitar confusiones, podemos usar @SerialName.
    @SerialName("id_Residente")
    val residenteId: Int
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class User(
    @SerialName("id")
    val id: Int,

    // Añade el '?' para permitir que el valor pueda ser nulo
    @SerialName("username")
    val username: String?,

    // Añade el '?' para permitir que el valor pueda ser nulo
    @SerialName("password")
    val password: String?
)


//APIIIIIIIIII
private const val BASE_URL_MIAPI = "https://49819bf01725.ngrok-free.app"

private val retrofit = Retrofit.Builder()
    // Asegúrate que el convertidor puede ignorar llaves desconocidas en el JSON
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL_MIAPI)
    .build()



// Interfaz ApiService en ApiService.kt

interface ApiService {
    // El endpoint de login parece correcto.
    @POST("api/Users/auth/login") // Ruta completa del controlador de Users
    suspend fun login(@Body request: LoginRequest): User

    // Según UsersController.cs, el endpoint para obtener invitados es "api/Residentes"
    // pero está dentro de UsersController. La ruta completa podría ser "api/Users/api/Residentes"
    // Esto parece un error en la ruta del backend, pero nos adaptamos.
    // La API espera el ID del usuario en la cabecera "xUserId".
    @GET("api/Users/api/Residentes")
    suspend fun getInvitados(@Header("xUserId") userId: Int): List<Invitado>

    // Para registrar un invitado, el endpoint correcto debería ser "api/Invitados"
    // y no requiere el ID de usuario en el header según InvitadosController.cs
    @POST("api/Invitados")
    suspend fun registrarInvitado(@Body invitado: NuevoInvitadoRequest): Invitado
}

object AppApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}