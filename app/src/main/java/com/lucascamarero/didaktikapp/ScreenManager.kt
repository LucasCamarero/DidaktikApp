package com.lucascamarero.didaktikapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.components.TopBar
import com.lucascamarero.didaktikapp.screens.MapScreen
import com.lucascamarero.didaktikapp.screens.activities.*
import com.lucascamarero.didaktikapp.screens.activities.commons.EndOfActivityScreen
import com.lucascamarero.didaktikapp.screens.activities.commons.StartOfActivityScreen
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.Diploma
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.JoinThePhotos
import com.lucascamarero.didaktikapp.screens.activities.finalactivity.WriteSentence
import com.lucascamarero.didaktikapp.screens.selectLanguage
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel
import kotlinx.coroutines.launch

/**
 * Gestor principal de pantallas de la aplicación.
 *
 * Se encarga de:
 * - Configurar la navegación mediante `NavHost`.
 * - Gestionar el `ModalNavigationDrawer`.
 * - Mostrar la barra superior (`TopBar`).
 * - Controlar el cambio de idioma desde el menú lateral.
 *
 * Esta función actúa como punto central de la UI una vez iniciada la app.
 *
 * @param languageViewModel ViewModel responsable de gestionar el idioma de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager(languageViewModel: LanguageViewModel) {

    /**
     * Controlador de navegación de Compose.
     */
    val navController = rememberNavController()

    /**
     * ViewModel que gestiona el contador de progreso del usuario.
     */
    val counterViewModel: CounterViewModel = hiltViewModel()

    /**
     * Estado del menú lateral (drawer).
     */
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    /**
     * Scope para ejecutar operaciones de apertura/cierre del drawer.
     */
    val scope = rememberCoroutineScope()

    /**
     * Indica si se muestra el selector de idiomas dentro del drawer.
     */
    var showLanguageCard by remember { mutableStateOf(false) }

    /**
     * Drawer lateral de navegación.
     *
     * Los gestos están desactivados para evitar conflictos con el mapa.
     */
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                drawerContentColor = MaterialTheme.colorScheme.scrim
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    /**
                     * Selector de idiomas.
                     *
                     * Se muestra u oculta dinámicamente dentro del drawer.
                     */
                    if (showLanguageCard) {
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))

                        BoxWithConstraints {
                            val isLandscape = maxWidth > maxHeight

                            LanguageCard(
                                isLandscape = isLandscape,
                                onLanguageSelected = { lang ->
                                    selectLanguage(lang, languageViewModel)
                                    showLanguageCard = false
                                }
                            )
                        }
                    }

                    /**
                     * Título del menú.
                     */
                    Text(
                        text = stringResource(id = R.string.menu_name),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.padding(vertical = 15.dp))

                    /**
                     * Opción para cambiar el idioma.
                     */
                    CreateNavigationDrawerItem(
                        text = stringResource(id = R.string.menu_change_language),
                        icon = Icons.Filled.Language,
                        onClick = {
                            showLanguageCard = !showLanguageCard
                        }
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    /**
                     * Opción para reiniciar el juego.
                     */
                    CreateNavigationDrawerItem(
                        text = stringResource(id = R.string.restart),
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            // a desarrollar
                        }
                    )

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    /**
                     * Opción para cerrar el menú lateral.
                     */
                    CreateNavigationDrawerItem(
                        text = stringResource(id = R.string.exit),
                        icon = Icons.Filled.ExitToApp,
                        onClick = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {

        /**
         * Scaffold principal que contiene la barra superior y el contenido.
         */
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    counterViewModel = counterViewModel,
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /**
                 * Host de navegación de la aplicación.
                 */
                NavHost(
                    navController = navController,
                    startDestination = "map"
                ) {

                    /** Pantalla principal (mapa) */
                    composable("map") { MapScreen(navController, counterViewModel) }

                    /** Pantalla de introducción genérica de actividades */
                    composable(
                        route = "startactivity/{number}",
                        arguments = listOf(navArgument("number") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 1
                        StartOfActivityScreen(navController, number)
                    }

                    /** Pantalla de final genérica de actividades */
                    composable(
                        route = "endactivity/{number}",
                        arguments = listOf(navArgument("number") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 1
                        EndOfActivityScreen(navController, number)
                    }

                    /** Pantallas de actividades */
                    composable("activity1") { Activity1Screen(navController) }
                    composable("activity2") { Activity2Screen(navController) }
                    composable("activity3") { Activity3Screen(navController) }
                    composable("activity4") { Activity4Screen(navController) }
                    composable("activity5") { Activity5Screen(navController) }
                    composable("activity6") { Activity6Screen(navController) }
                    composable("activity7") { Activity7Screen(navController) }

                    /** Actividad final */
                    composable("jointhephotos") { JoinThePhotos(navController) }
                    composable("writesentence") { WriteSentence(navController) }

                    /** Diploma final */
                    composable("diploma") { Diploma(navController) }
                }
            }
        }
    }
}

/**
 * Elemento reutilizable del menú lateral.
 *
 * Representa una opción del `NavigationDrawer` con icono y texto.
 *
 * @param text Texto que se muestra en el item.
 * @param icon Icono asociado a la opción.
 * @param selected Indica si el item está seleccionado.
 * @param onClick Acción que se ejecuta al pulsar el item.
 */
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
                tint = MaterialTheme.colorScheme.scrim
            )
        },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedTextColor = MaterialTheme.colorScheme.scrim
        )
    )
}
