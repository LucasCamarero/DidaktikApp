package com.lucascamarero.didaktikapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucascamarero.didaktikapp.screens.IntroScreen
import com.lucascamarero.didaktikapp.screens.activities.Activity1Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity2Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity3Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity4Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity5Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity6Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity7Screen
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.Diploma
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.JoinThePhotos
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.WriteSentence
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager() {
    val navController = rememberNavController()
    // instancias de view models
    val counterViewModel: CounterViewModel = viewModel()
    NavHost(navController = navController,
        startDestination = "Intro")
    {
        composable("Intro") { IntroScreen(navController) }
        composable("Activity1") { Activity1Screen(navController) }
        composable("Activity2") { Activity2Screen(navController) }
        composable("Activity3") { Activity3Screen(navController) }
        composable("Activity4") { Activity4Screen(navController) }
        composable("Activity5") { Activity5Screen(navController) }
        composable("Activity6") { Activity6Screen(navController) }
        composable("Activity7") { Activity7Screen(navController) }
        composable("dimpola") { Diploma(navController) }
        composable("ActivityFinal") { WriteSentence(navController) }
        composable("jolinFinal") { JoinThePhotos(navController) }
    }
}
