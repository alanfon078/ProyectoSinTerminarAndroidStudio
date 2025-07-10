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
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    @SerialName("id_Residente")
    val residenteId: Int
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class User(
    @SerialName("id")
    val id: Int,

    @SerialName("username")
    val username: String?,

    @SerialName("password")
    val password: String?
)


//APIIIIIIIIII
private const val BASE_URL_MIAPI = "https://49819bf01725.ngrok-free.app"

private val retrofit = Retrofit.Builder()
    // El conertidor ignora las llaves desconocidas que vengan en el JSON
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL_MIAPI)
    .build()



interface ApiService {

    @POST("api/Users/auth/login")
    suspend fun login(@Body request: LoginRequest): User

    @GET("api/Users/api/Residentes")
    suspend fun getInvitados(@Header("xUserId") userId: Int): List<Invitado>
    @POST("api/Invitados")
    suspend fun registrarInvitado(@Body invitado: NuevoInvitadoRequest): Invitado
}

object AppApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}