package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

/**
 * Barra superior de la aplicación.
 *
 * Muestra:
 * - Un botón de ajustes que abre el menú lateral.
 * - El avatar de Jolin con un contador de progreso.
 * - Un botón para volver a la pantalla principal (mapa).
 *
 * Esta barra se utiliza como `TopAppBar` dentro del `Scaffold`.
 *
 * @param navController Controlador de navegación para gestionar la acción "Home".
 * @param counterViewModel ViewModel que proporciona el valor del contador mostrado.
 * @param onMenuClick Acción que se ejecuta al pulsar el botón de ajustes (abrir/cerrar drawer).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    counterViewModel: CounterViewModel,
    onMenuClick: () -> Unit
) {
    /**
     * Estado actual del contador recogido desde el ViewModel.
     */
    val currentCount by counterViewModel.count.collectAsState()

    TopAppBar(
        title = {},

        /**
         * Contenido personalizado del área de navegación.
         *
         * Se utiliza un `Row` para distribuir los elementos horizontalmente.
         */
        navigationIcon = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                /**
                 * Botón de ajustes.
                 *
                 * Abre o cierra el menú lateral.
                 */
                IconButton(onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Abrir menú",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.size(35.dp)
                    )
                }

                /**
                 * Avatar de Jolin con badge de contador.
                 *
                 * El badge muestra el progreso actual del usuario.
                 */
                BadgedBox(
                    badge = {
                        Badge(
                            modifier = Modifier.size(25.dp),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Text(currentCount.toString())
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rostrojolin),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                /**
                 * Botón Home.
                 *
                 * Navega a la pantalla principal del mapa.
                 */
                IconButton(onClick = { navController.navigate("map") }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },

        /**
         * Colores personalizados de la barra superior.
         */
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.scrim,
            navigationIconContentColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.scrim
        )
    )
}
