package com.example.proyecto.network

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

data class Invitado (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val fechaVisita: String,
    val token: String,
    val residenteId : Int,
    val residente: Residente? = null
)
