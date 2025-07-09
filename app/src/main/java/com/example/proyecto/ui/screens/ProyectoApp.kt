package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.ui.screens.*

// Enum para las rutas, evita errores de tipeo
enum class AppScreen {
    Login,
    GuestList,
    RegisterGuest,
    QrCode
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoApp() {
    val navController = rememberNavController()
    val appViewModel: AppViewModel = viewModel()
    val uiState = appViewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Control de Acceso") })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Login.name) {
                LoginScreen(
                    onLoginClick = { user, pass -> appViewModel.login(user, pass) },
                    uiState = uiState
                )
            }
            composable(AppScreen.GuestList.name) {
                if (uiState is AppUiState.Success) {
                    GuestListScreen(
                        invitados = uiState.invitados,
                        onAddGuestClick = { navController.navigate(AppScreen.RegisterGuest.name) },
                        onLogoutClick = { appViewModel.logout() }
                    )
                }
            }
            composable(AppScreen.RegisterGuest.name) {
                RegisterGuestScreen(
                    onRegisterClick = { request ->
                        appViewModel.registrarInvitado(request) { invitadoCreado ->
                            if (invitadoCreado != null) {
                                // Navega a la pantalla del QR, pasando el token del invitado
                                navController.navigate("${AppScreen.QrCode.name}/${invitadoCreado.token}")
                            }
                        }
                    }
                )
            }
            composable("${AppScreen.QrCode.name}/{qr_token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("qr_token") ?: ""
                QrCodeScreen(
                    qrContent = token,
                    viewModel = appViewModel
                )
            }
        }
    }
    // Cambiar el LaunchedEffect para que reaccione a cambios en el ID de usuario
    LaunchedEffect(appViewModel.currentUserId) {
        appViewModel.currentUserId?.let { userId ->
            if (navController.currentDestination?.route != AppScreen.GuestList.name) {
                navController.navigate(AppScreen.GuestList.name) {
                    popUpTo(AppScreen.Login.name) { inclusive = true }
                }
            }
        }
    }
}