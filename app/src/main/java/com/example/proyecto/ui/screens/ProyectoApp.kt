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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Enumerados para cada ruta
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
            TopAppBar(title = { Text("Control de Acceso de Invitados") })
        }
    ) { innerPadding ->

        // Se activa cuando se inicia sesion
        LaunchedEffect(appViewModel.currentUserId) {
            if (appViewModel.currentUserId != null) {
                navController.navigate(AppScreen.GuestList.name) {
                    popUpTo(AppScreen.Login.name) { inclusive = true }
                }
            }
        }

        // Se activa cuando se cierra sesion
        LaunchedEffect(uiState) {
            if (uiState is AppUiState.SignedOut) {
                navController.navigate(AppScreen.Login.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }

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
                    is AppUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is AppUiState.Success -> {
                        GuestListScreen(
                            invitados = currentState.invitados,
                            onAddGuestClick = { navController.navigate(AppScreen.RegisterGuest.name) },
                            onLogoutClick = { appViewModel.logout() }
                        )
                    }
                    is AppUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error al cargar los invitados: ${currentState.message}")
                        }
                    }
                    is AppUiState.SignedOut -> {

                    }
                }
            }
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
            composable("${AppScreen.QrCode.name}/{qr_token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("qr_token") ?: ""
                QrCodeScreen(
                    qrContent = token,
                    viewModel = appViewModel,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}