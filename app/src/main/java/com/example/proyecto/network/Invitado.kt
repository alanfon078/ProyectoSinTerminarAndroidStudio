package com.example.proyecto.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Invitado(
    @SerialName("id_Invitado")
    val id: Int,

    @SerialName("nombre")
    val nombre: String,

    @SerialName("apellidos")
    val apellidos: String,

    @SerialName("telefono")
    val telefono: String,

    @SerialName("token")
    val token: String?,

    @SerialName("id_Residente")
    val residenteId: Int

)
