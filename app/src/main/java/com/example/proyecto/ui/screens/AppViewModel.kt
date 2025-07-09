package com.example.proyecto.ui.screens

import android.graphics.Bitmap
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

sealed interface AppUiState {
    object SignedOut : AppUiState
    object Loading : AppUiState
    data class Success(val invitados: List<Invitado>) : AppUiState
    data class Error(val message: String) : AppUiState
}

class AppViewModel : ViewModel() {

    var uiState: AppUiState by mutableStateOf(AppUiState.SignedOut)
        private set

    var currentUserId: Int? by mutableStateOf(null) // Guarda el ID en lugar del token


    // Modificar la función login
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
                        e is HttpException && e.code() == 401 ->
                            "Usuario o contraseña incorrectos"
                        e is IOException ->
                            "Error de conexión. Verifique su internet"
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
                    uiState = AppUiState.Error(message =  "Error inesperado: ${e.message}")
                }
            } ?: run {
                uiState = AppUiState.SignedOut
            }
        }
    }

    fun registrarInvitado(request: NuevoInvitadoRequest, onResult: (Invitado?) -> Unit) {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                try {
                    val invitadoCreado = AppApi.retrofitService.registrarInvitado(userId, request)
                    getInvitados()
                    onResult(invitadoCreado)
                } catch (e: Exception) {
                    onResult(null)
                }
            } ?: onResult(null)
        }
    }

    fun generarQrBitmap(qrContent: String, size: Int): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, size, size)
    }

    fun logout() {
        currentUserId = null
        uiState = AppUiState.SignedOut
    }
}