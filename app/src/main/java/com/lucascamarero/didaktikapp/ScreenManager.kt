package com.lucascamarero.didaktikapp

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.components.TopBar
import com.lucascamarero.didaktikapp.models.AppLanguage
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
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager(languageViewModel: LanguageViewModel) {

    var navController = rememberNavController()

    // instancias de view models
    val counterViewModel: CounterViewModel = hiltViewModel() // Usa hiltViewModel()
    // Estado del drawer y scope para abrir/cerrar
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showLanguageCard by remember { mutableStateOf(false) }

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                drawerContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ){
                Column(Modifier
                    .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Text(stringResource(id = R.string.menu_name),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.padding(vertical = 15.dp))

                    CreateNavigationDrawerItem(
                        text = stringResource(id = R.string.menu_change_language),
                        icon = Icons.Filled.Language,
                        onClick = {
                            showLanguageCard = !showLanguageCard
                        }
                    )

                    // Sale a Home, sin matar el proceso
                    CreateNavigationDrawerItem(
                        text = stringResource(id = R.string.exit),
                        icon = Icons.Filled.ExitToApp,
                        onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_MAIN).apply {
                                    addCategory(Intent.CATEGORY_HOME)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            )
                        }
                    )

                    if (showLanguageCard) {
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))

                        LanguageCard { lang ->
                            when (lang) {
                                "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
                                "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
                                "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
                            }
                            // Si quieres ocultarla después:
                            showLanguageCard = false
                            scope.launch { drawerState.close() }
                        }
                    }

                    CreateNavigationDrawerItem(
                        text = "Mapa",
                        icon = Icons.Filled.Map,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("map")
                        }
                    )

                    for (i in 1..7) {
                        CreateNavigationDrawerItem(
                            text = "Juego $i",
                            icon = Icons.Filled.VideogameAsset,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("startactivity/$i")
                            }
                        )
                    }

                    CreateNavigationDrawerItem(
                        text = "Juego final",
                        icon = Icons.Filled.Star,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("jointhephotos")
                        }
                    )

                    CreateNavigationDrawerItem(
                        text = "Diploma",
                        icon = Icons.Filled.School,
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
                    navController = navController,
                    counterViewModel,
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "map"
                ) {
                    composable("map") { MapScreen(navController) }

                    // RUTA DE INTRODUCCIÓN (Genérica para todos los juegos)
                    composable(
                        route = "startactivity/{number}",
                        arguments = listOf(navArgument("number") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 1
                        StartOfActivityScreen(navController, number)
                    }

                    // RUTAS DE LOS JUEGOS REALES
                    composable("activity1") {
                        Activity1Screen(onNavigateBack = { navController.popBackStack() })
                    }

                    // El resto de actividades...
                    composable("activity2") { Activity2Screen(navController) }
                    // ...
                    composable("diploma") { Diploma(navController) }
                    composable("jointhephotos") { JoinThePhotos(navController) }
                }
            }
        }
    }
}

@Composable
fun Acitivity1Screen(onNavigateBack: () -> Boolean) {
    TODO("Not yet implemented")
}

@Composable
fun CreateNavigationDrawerItem(
    text: String,
    icon: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        label = {
            Text(
                text,
                style = MaterialTheme.typography.labelMedium
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}