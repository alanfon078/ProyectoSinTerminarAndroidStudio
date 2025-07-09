package com.example.proyecto.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

//@kotlinx.serialization.Serializable
data class LoginRequest(val usuario: String, val contrasena: String)
//@kotlinx.serialization.Serializable
data class LoginResponse(val token: String)


data class NuevoInvitadoRequest(
    val id: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val fechaVisita: String,
    val residenteId: Int
)

data class User(
    val id: Int,
    val username: String,
    val password: String
)


//APIIIIIIIIII
private const val BASE_URL_MIAPI = "https://1590ad3bba09.ngrok-free.app"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL_MIAPI)
    .build()

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): User // Cambia LoginResponse por User

    @GET("api/Residentes")
    suspend fun getInvitados(@Header("X-User-ID") userId: Int): List<Invitado> // Usa el ID como header

    @POST("api/Residentes")
    suspend fun registrarInvitado(
        @Header("X-User-ID") userId: Int,
        @Body invitado: NuevoInvitadoRequest
    ): Invitado
}

object AppApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}