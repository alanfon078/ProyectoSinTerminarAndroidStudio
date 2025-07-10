package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.network.Invitado

@Composable
fun GuestListScreen(
    invitados: List<Invitado>,
    onAddGuestClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddGuestClick) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Invitado")
            }
        },
        topBar = {
            // La TopBar se maneja en ProyectoApp, puedes dejar esto vacío si quieres.
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Mis Invitados", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = onLogoutClick) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                }
            }
            Spacer(Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(invitados) { invitado ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // **AQUÍ ESTÁN LOS CAMBIOS**
                            // Usamos las propiedades correctas del nuevo modelo: nombre y apellidos.
                            Text(
                                "${invitado.nombre} ${invitado.apellidos}", // Usamos "apellidos"
                                style = MaterialTheme.typography.titleLarge
                            )
                            // Mostramos solo el teléfono, ya que email y fecha no existen en el modelo.
                            Text("Teléfono: ${invitado.telefono}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}