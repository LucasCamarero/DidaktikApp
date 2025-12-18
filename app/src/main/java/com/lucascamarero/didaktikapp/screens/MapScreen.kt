package com.lucascamarero.didaktikapp.screens

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.models.MapPoint
import com.lucascamarero.didaktikapp.ui.theme.Typography3
import com.lucascamarero.didaktikapp.utils.createNumberedMarker
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Pantalla que muestra el mapa con las actividades geolocalizadas.
 *
 * Esta pantalla utiliza un mapa basado en OSMDroid y representa cada actividad
 * mediante un marcador numerado. El estado visual y la interacción de los
 * marcadores dependen del progreso del usuario.
 *
 * Estados de los marcadores:
 * - **Completada**: marcador con fondo oscuro y número claro.
 * - **Actual**: marcador con colores por defecto.
 * - **Bloqueada**: muestra un mensaje informativo mediante un `Snackbar`.
 *
 * El acceso a las actividades está forzado en orden secuencial.
 *
 * @param navController Controlador de navegación para acceder a las pantallas de actividades.
 * @param counterViewModel ViewModel que proporciona el progreso del usuario.
 */
@Composable
fun MapScreen(
    navController: NavController,
    counterViewModel: CounterViewModel
) {
    /**
     * Número de actividades completadas, observado desde la base de datos.
     */
    val count = counterViewModel.count.collectAsState().value + 4

    /**
     * Identificador de la siguiente actividad disponible.
     */
    val nextActivity = count + 1

    /**
     * Colores convertidos desde Compose a formato Android (ARGB),
     * utilizados para los marcadores completados.
     */
    val completedBg = MaterialTheme.colorScheme.scrim.toArgb()
    val completedText = MaterialTheme.colorScheme.secondaryContainer.toArgb()

    /**
     * Estado y scope para mostrar mensajes temporales mediante Snackbar.
     */
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /**
     * Lista de puntos del mapa que representan las actividades.
     *
     * Actualmente están definidos de forma estática.
     */
    val mapPoints = listOf(
        MapPoint(1, 43.257611, -2.979528, "Ermita Santa Águeda"),
        MapPoint(2, 43.295750, -2.996722, "Iglesia de San Vicente"),
        MapPoint(3, 43.296902, -2.987188, "Mercado de Abastos"),
        MapPoint(4, 43.302592, -2.985833, "Edificio Ilgner"),
        MapPoint(5, 43.315600, -3.010764, "Cargadero de minas"),
        MapPoint(6, 43.305550, -2.982158, "Ferrocarril"),
        MapPoint(7, 43.295125, -2.978303, "Palacio Munoa")
    )

    /**
     * Contenedor principal que superpone el mapa y el Snackbar.
     */
    Box(modifier = Modifier.fillMaxSize()) {

        /**
         * Vista del mapa integrada mediante AndroidView.
         */
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {

                    // Configuración del mapa base
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(43.29, -2.99))

                    // Creación y configuración de los marcadores
                    mapPoints.forEach { point ->

                        val marker = Marker(this).apply {
                            position = GeoPoint(point.lat, point.lng)
                            title = point.name
                            subDescription = "Actividad ${point.id}"
                            setAnchor(
                                Marker.ANCHOR_CENTER,
                                Marker.ANCHOR_BOTTOM
                            )
                        }

                        /**
                         * Selección del icono del marcador según el estado de la actividad.
                         */
                        val markerBitmap = when {
                            // Actividad completada
                            point.id <= count -> {
                                createNumberedMarker(
                                    context = context,
                                    number = point.id,
                                    backgroundColor = completedBg,
                                    textColor = completedText
                                )
                            }

                            // Actividad actual
                            point.id == nextActivity -> {
                                createNumberedMarker(
                                    context = context,
                                    number = point.id
                                )
                            }

                            // Actividad bloqueada
                            else -> {
                                createNumberedMarker(
                                    context = context,
                                    number = point.id
                                )
                            }
                        }

                        marker.icon = BitmapDrawable(resources, markerBitmap)

                        /**
                         * Gestión de la interacción con el marcador.
                         */
                        marker.setOnMarkerClickListener { _, _ ->
                            when {
                                // Navegar a la actividad actual
                                point.id == nextActivity -> {
                                    navController.navigate("startactivity/${point.id}") {
                                        launchSingleTop = true
                                    }
                                }

                                // Permitir repetir actividades completadas
                                point.id <= count -> {
                                    navController.navigate("startactivity/${point.id}")
                                }

                                // Mostrar mensaje si la actividad está bloqueada
                                else -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Primero completa la actividad $nextActivity",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                            true
                        }

                        overlays.add(marker)
                    }
                }
            }
        )

        /**
         * Snackbar personalizado centrado en pantalla.
         *
         * Se muestra cuando el usuario intenta acceder a una actividad bloqueada.
         */
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.Center),
            snackbar = { snackbarData ->

                Card(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .wrapContentWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        style = Typography3.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        )
    }
}

