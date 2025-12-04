package com.lucascamarero.didaktikapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucascamarero.didaktikapp.components.TopBar
import com.lucascamarero.didaktikapp.screens.MapScreen
import com.lucascamarero.didaktikapp.screens.activities.Activity1Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity2Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity3Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity4Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity5Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity6Screen
import com.lucascamarero.didaktikapp.screens.activities.Activity7Screen
import com.lucascamarero.didaktikapp.screens.activities.commons.EndOfActivityScreen
import com.lucascamarero.didaktikapp.screens.activities.commons.StartOfActivityScreen
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.Diploma
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.JoinThePhotos
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.WriteSentence
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager() {

    val navController = rememberNavController()
    val counterViewModel: CounterViewModel = viewModel()

    // Estado del drawer y scope para abrir/cerrar
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(16.dp)) {
                    Text("Menú")

                    NavigationDrawerItem(
                        label = { Text("Mapa", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("map")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 1", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/1")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 2", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/2")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 3", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/3")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 4", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/4")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 5", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/5")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 6", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/6")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego 7", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("startactivity/7")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Juego final", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("jointhephotos")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Diploma", style = MaterialTheme.typography.labelLarge) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("diploma")
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "map"
                ) {
                    composable("map") { MapScreen(navController) }
                    composable(
                        route = "startactivity/{number}",
                        arguments = listOf(
                            navArgument("number") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 0
                        StartOfActivityScreen(navController, number)
                    }
                    composable(
                        route = "endactivity/{number}",
                        arguments = listOf(
                            navArgument("number") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 0
                        EndOfActivityScreen(navController, number)
                    }
                    composable("activity1") { Activity1Screen(navController) }
                    composable("activity2") { Activity2Screen(navController) }
                    composable("activity3") { Activity3Screen(navController) }
                    composable("activity4") { Activity4Screen(navController) }
                    composable("activity5") { Activity5Screen(navController) }
                    composable("activity6") { Activity6Screen(navController) }
                    composable("activity7") { Activity7Screen(navController) }
                    composable("diploma") { Diploma(navController) }
                    composable("writesentence") { WriteSentence(navController) }
                    composable("jointhephotos") { JoinThePhotos(navController) }
                }
            }
        }
    }
}
