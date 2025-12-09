package com.lucascamarero.didaktikapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel
import com.lucascamarero.didaktikapp.screens.IntroScreen
import com.lucascamarero.didaktikapp.screens.activities.Acitivity1Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager() {

    val navController = rememberNavController()

    // Instancia de ViewModels (solo se deja CounterViewModel como ejemplo)
    val counterViewModel: CounterViewModel = viewModel()

    // DEFINIMOS EL NAVHOST
    NavHost(
        navController = navController,
        // TEMPORAL: Usamos Actividad1Screen como pantalla de inicio para probar
        startDestination = "actividad1"
    ) {
        // Pantalla de la actividad 1 (Ermita Santa Águeda)
        composable("actividad1") {
            Acitivity1Screen(
                onNavigateBack = {
                    // Esto saca la Actividad 1 de la pila y vuelve a la anterior (Mapa o Intro)
                    navController.popBackStack()

                    // O si quieres ir a una pantalla específica (ej. Mapa):
                    // navController.navigate("ruta_mapa") {
                    //     popUpTo("actividad1") { inclusive = true }
                    // }
                }
            )
        }

        // Ejemplo de otra pantalla
        composable("intro") {
            IntroScreen()
        }

        // ... aquí irán tus otras rutas: "login", "ruta_mapa", etc.
    }
}