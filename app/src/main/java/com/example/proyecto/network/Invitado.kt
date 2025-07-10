package com.example.proyecto.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Invitado(
    // Mapea "Id_Invitado" del JSON a "id" en Kotlin
    @SerialName("Id_Invitado")
    val id: Int,

    // Mapea "Nombre" a "nombre"
    @SerialName("Nombre")
    val nombre: String,

    // Mapea "Apellidos" a "apellidos"
    @SerialName("Apellidos")
    val apellidos: String,

    // Mapea "Telefono" a "telefono"
    @SerialName("Telefono")
    val telefono: String,

    // Mapea "Token", que puede ser nulo
    @SerialName("Token")
    val token: String?,

    // Mapea "Id_Residente" a "residenteId"
    @SerialName("Id_Residente")
    val residenteId: Int,

    // El objeto Residente anidado se omite si no lo envías desde la API,
    // pero lo dejamos como nulo por si lo agregas en el futuro.
    @SerialName("Residente")
    val residente: Residente? = null
)
