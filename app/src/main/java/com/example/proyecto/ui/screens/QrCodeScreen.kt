package com.example.proyecto.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.screens.AppViewModel

@Composable
fun QrCodeScreen(qrContent: String, viewModel: AppViewModel) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(qrContent) {
        if (qrContent.isNotEmpty()) {
            qrBitmap = viewModel.generarQrBitmap(qrContent, 800) // QR de 800x800px
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        qrBitmap?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Comparte este QR con tu invitado", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(24.dp))
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Código QR de acceso",
                    modifier = Modifier.size(300.dp)
                )
            }
        } ?: CircularProgressIndicator()
    }
}