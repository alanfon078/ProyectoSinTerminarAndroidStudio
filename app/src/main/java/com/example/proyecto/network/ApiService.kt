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
    @SerialName("Nombre")
    val nombre: String,

    @SerialName("Apellidos")
    val apellidos: String,

    // El email no está en el modelo del backend, lo eliminamos.

    @SerialName("Telefono")
    val telefono: String,

    // La fecha de visita no está en el modelo, la eliminamos.

    @SerialName("Id_Residente")
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
private const val BASE_URL_MIAPI = "https://73d4f78c41bc.ngrok-free.app"

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