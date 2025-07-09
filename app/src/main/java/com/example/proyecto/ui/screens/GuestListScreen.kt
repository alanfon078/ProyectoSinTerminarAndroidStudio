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
                            Text(
                                "${invitado.nombre} ${invitado.apellido}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text("Email: ${invitado.email}", style = MaterialTheme.typography.bodyMedium)
                            Text("Teléfono: ${invitado.telefono}", style = MaterialTheme.typography.bodyMedium)
                            Text("Fecha de Visita: ${invitado.fechaVisita}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}