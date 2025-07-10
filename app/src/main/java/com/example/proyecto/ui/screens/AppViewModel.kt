package com.example.proyecto.ui.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.network.AppApi
import com.example.proyecto.network.Invitado
import com.example.proyecto.network.LoginRequest
import com.example.proyecto.network.NuevoInvitadoRequest
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

sealed interface AppUiState {
    object SignedOut : AppUiState
    object Loading : AppUiState
    data class Success(val invitados: List<Invitado>) : AppUiState
    data class Error(val message: String) : AppUiState
}

class AppViewModel : ViewModel() {

    var uiState: AppUiState by mutableStateOf(AppUiState.SignedOut)
        private set

    var currentUserId: Int? by mutableStateOf(null)

    fun login(usuario: String, contrasena: String) {
        viewModelScope.launch {
            uiState = AppUiState.Loading
            try {

                val user = AppApi.retrofitService.login(LoginRequest(usuario, contrasena))
                currentUserId = user.id
                getInvitados()
            } catch (e: Exception) {
                uiState = AppUiState.Error(
                    message = when {
                        e is HttpException && e.code() == 401 -> "Usuario o contraseña incorrectos"
                        e is IOException -> "Error de conexión. Verifique su internet"
                        else -> "Error desconocido: ${e.message}"
                    }
                )
            }
        }
    }

    private fun getInvitados() {
        viewModelScope.launch {
            uiState = AppUiState.Loading
            currentUserId?.let { userId ->
                try {
                    val invitados = AppApi.retrofitService.getInvitados(userId)
                    uiState = AppUiState.Success(invitados)
                } catch (e: Exception) {
                    uiState = AppUiState.Error(message =  "Error al cargar invitados: ${e.message}")
                }
            } ?: run {
                uiState = AppUiState.SignedOut
            }
        }
    }

    fun registrarInvitado(
        requestData: Pair<String, String>,
        telefono: String,
        onResult: (Invitado?) -> Unit
    ) {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                try {
                    val request = NuevoInvitadoRequest(
                        nombre = requestData.first,
                        apellidos = requestData.second,
                        telefono = telefono,
                        residenteId = userId
                    )
                    val invitadoCreado = AppApi.retrofitService.registrarInvitado(request)
                    getInvitados()
                    onResult(invitadoCreado)
                } catch (e: Exception) {
                    Log.e("AppViewModel", "Error al registrar invitado: ${e.message}")
                    onResult(null)
                }
            } ?: onResult(null)
        }
    }

    fun generarQrBitmap(qrContent: String, size: Int): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, size, size)
    }

    fun shareQrBitmap(context: Context, bitmap: Bitmap?) {
        if (bitmap == null) return

        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "qr_code.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()


            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )


            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, contentUri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }


            val chooser = Intent.createChooser(shareIntent, "Compartir código QR vía...")
            context.startActivity(chooser)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout() {
        uiState = AppUiState.SignedOut
        currentUserId = null
    }
}