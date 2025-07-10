package com.example.proyecto.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScreen(
    qrContent: String,
    viewModel: AppViewModel,
    onNavigateUp: () -> Unit
) {
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
                qrBitmap = viewModel.generarQrBitmap(qrContent, 800) // QR de 800x800px
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            qrBitmap?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Comparte este QR con tu invitado",
                        style = MaterialTheme.typography.headlineSmall
                    )
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
}