package com.example.proyecto.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Invitado(
    @SerialName("id_Invitado") // Asegúrate que coincida con el JSON (puede ser "id_Invitado" o "idInvitado")
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
    // El objeto Residente anidado se omite porque la API no lo envía en esta llamada.
)
