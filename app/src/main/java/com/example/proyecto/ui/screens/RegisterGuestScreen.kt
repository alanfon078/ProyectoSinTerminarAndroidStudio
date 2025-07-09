package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.network.NuevoInvitadoRequest

@Composable
fun RegisterGuestScreen(onRegisterClick: (NuevoInvitadoRequest) -> Unit) {
    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaVisita by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Registrar Nuevo Invitado", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("id") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = fechaVisita, onValueChange = { fechaVisita = it }, label = { Text("Fecha de Visita (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val request = NuevoInvitadoRequest(
                    id = id,
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    telefono = telefono,
                    fechaVisita = fechaVisita,
                    residenteId = 4
                )
                onRegisterClick(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar y Generar QR")
        }

    }
}