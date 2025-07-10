package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
                when (val currentState = uiState) {
                    // Muestra un indicador de carga mientras se obtienen los datos
                    is AppUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    // Muestra la lista de invitados si todo salió bien
                    is AppUiState.Success -> {
                        GuestListScreen(
                            invitados = currentState.invitados,
                            onAddGuestClick = { navController.navigate(AppScreen.RegisterGuest.name) },
                            onLogoutClick = { appViewModel.logout() }
                        )
                    }
                    // Muestra un mensaje de error si algo falló
                    is AppUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error al cargar los invitados: ${currentState.message}")
                        }
                    }
                    // No hagas nada si la sesión está cerrada (SignedOut)
                    is AppUiState.SignedOut -> {
                        // No se muestra nada, la navegación debería redirigir al login.
                    }
                }
            }

            // Dentro de tu NavHost en ProyectoApp.kt

            composable(AppScreen.RegisterGuest.name) {
                RegisterGuestScreen(
                    onRegisterClick = { nombreApellidoPair, telefono ->
                        appViewModel.registrarInvitado(nombreApellidoPair, telefono) { invitadoCreado ->
                            if (invitadoCreado != null) {
                                invitadoCreado.token?.let { token ->
                                    navController.navigate("${AppScreen.QrCode.name}/${token}")
                                }
                            }
                        }
                    },
                    onNavigateUp = { navController.navigateUp() }
                )
            }

// Llamada a QrCodeScreen
            composable("${AppScreen.QrCode.name}/{qr_token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("qr_token") ?: ""
                QrCodeScreen(
                    qrContent = token,
                    viewModel = appViewModel,
                    // Pasa la función para navegar hacia atrás
                    onNavigateUp = { navController.navigateUp() }
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