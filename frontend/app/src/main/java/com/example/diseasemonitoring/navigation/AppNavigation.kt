package com.example.diseasemonitoring.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diseasemonitoring.screens.HomeScreen
import com.example.diseasemonitoring.screens.LoginScreen
import com.example.diseasemonitoring.screens.RegisterScreen
import com.example.diseasemonitoring.viewmodels.DiseaseViewModels

@Composable
fun AppNavigation(viewModel: DiseaseViewModels) {
    val navController = rememberNavController()

    // Navigation principale
    NavHost(navController = navController, startDestination = "login") {
        // Écran de connexion
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // Écran d'inscription
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Écran d'accueil (HomeScreen)
        composable("home") {
            HomeScreen(
                diseaseList = viewModel.diseaseList.value ?: emptyList(),
                viewModel = viewModel,
                showAddDiseaseDialog = false, // Placeholder pour une logique future
                onShowAddDiseaseDialogChange = { /* Ajouter une logique si nécessaire */ }
            )
        }
    }
}
