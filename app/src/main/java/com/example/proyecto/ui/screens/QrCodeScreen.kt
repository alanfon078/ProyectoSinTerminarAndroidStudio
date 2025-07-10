package com.example.proyecto.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScreen(
    qrContent: String,
    viewModel: AppViewModel,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current // Obtiene el contexto de la UI

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Código de Acceso") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(qrContent) {
            if (qrContent.isNotEmpty()) {
                qrBitmap = viewModel.generarQrBitmap(qrContent, 800)
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            qrBitmap?.let { bitmap ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Comparte este QR con tu invitado",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(24.dp))
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Código QR de acceso",
                        modifier = Modifier.size(300.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    // Botón para compartir
                    Button(onClick = { viewModel.shareQrBitmap(context, bitmap) }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                        Spacer(Modifier.width(8.dp))
                        Text("Compartir")
                    }
                }
            } ?: CircularProgressIndicator()
        }
    }
}