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

// ... (importaciones)

@Composable
fun RegisterGuestScreen(onRegisterClick: (NuevoInvitadoRequest) -> Unit) {
    // El "id" se genera en el backend, lo eliminamos del formulario.
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    // Eliminamos las variables para email y fechaVisita

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Registrar Nuevo Invitado", style = MaterialTheme.typography.headlineMedium)

        // Eliminamos el campo para el ID
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        // Eliminamos los TextFields para email y fechaVisita

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val request = NuevoInvitadoRequest(
                    nombre = nombre,
                    apellidos = apellido, // Corregido a "apellidos"
                    telefono = telefono,
                    residenteId = 4 // NOTA: Este ID sigue siendo fijo. Deberías pasarlo dinámicamente.
                )
                onRegisterClick(request)
            },
            modifier = Modifier.fillMaxWidth(),
            // Habilitamos el botón si los campos necesarios están llenos.
            enabled = nombre.isNotBlank() && apellido.isNotBlank() && telefono.isNotBlank()
        ) {
            Text("Registrar y Generar QR")
        }
    }
}