package com.lucascamarero.didaktikapp.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager() {

    val navController = rememberNavController()

    // instancias de view models
    val counterViewModel: CounterViewModel = viewModel()

}