package com.reto2.didaktikapp.screens

import android.util.Log
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reto2.didaktikapp.R
import com.reto2.didaktikapp.utils.createNumberedMarker
import com.reto2.didaktikapp.viewmodels.CounterViewModel
import com.reto2.didaktikapp.viewmodels.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    counterViewModel: CounterViewModel,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    /**
     * Número de actividades completadas, observado desde la base de datos.
     */
    val count = counterViewModel.count.collectAsState().value

    /**
     * Identificador de la siguiente actividad disponible.
     */
    val nextActivity = count + 1

    /**
     * Colores convertidos desde Compose a formato Android (ARGB),
     * utilizados para los marcadores completados.
     */
    val completedBg = MaterialTheme.colorScheme.tertiaryContainer.toArgb()
    val completedText = MaterialTheme.colorScheme.secondaryContainer.toArgb()

    val introMessage = stringResource(id = R.string.intro_map)

    /**
     * Estado y scope para mostrar mensajes temporales mediante Snackbar.
     */
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /**
     * Lista de puntos del mapa que representan las actividades.
     * Se obtienen desde la base de datos a través del MapViewModel.
     */
    val mapPoints by mapViewModel.mapPoints.collectAsState()

    /**
     * Contenedor principal que superpone el mapa y el Snackbar.
     */
    Box(modifier = Modifier.fillMaxSize()) {

        /**
         * Vista del mapa integrada mediante AndroidView.
         * Se actualiza cuando cambian los puntos del mapa desde la base de datos.
         */
        val mapViewRef = remember { mutableStateOf<MapView?>(null) }
        
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    // Configuración del mapa base
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(43.29, -2.99))
                    mapViewRef.value = this
                }
            },
            update = { mapView ->
                mapViewRef.value = mapView
            }
        )

        /**
         * Actualizar marcadores cuando cambian los mapPoints o el count.
         */
        LaunchedEffect(mapPoints, count) {
            // Esperar un poco para asegurar que el MapView esté listo
            kotlinx.coroutines.delay(100)
            
            val mapView = mapViewRef.value
            if (mapView == null) {
                Log.w("MapScreen", "MapView no está disponible todavía")
                return@LaunchedEffect
            }
            
            Log.d("MapScreen", "Actualizando marcadores. mapPoints.size = ${mapPoints.size}, count = $count")
            
            // Trabajar en el hilo principal para actualizar la UI
            withContext(Dispatchers.Main) {
                // Limpiar marcadores existentes
                mapView.overlays.clear()
                Log.d("MapScreen", "Marcadores limpiados. Creando ${mapPoints.size} marcadores nuevos")

                // Crear y configurar los marcadores con los datos actuales
                mapPoints.forEach { point ->
                    Log.d("MapScreen", "Creando marcador para punto: id=${point.id}, name=${point.name}, lat=${point.lat}, lng=${point.lng}")
                    
                    val marker = Marker(mapView).apply {
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
                    val currentNextActivity = count + 1
                    val markerBitmap = when {
                        // Actividad completada
                        point.id <= count -> {
                            createNumberedMarker(
                                context = mapView.context,
                                number = point.id,
                                backgroundColor = completedBg,
                                textColor = completedText
                            )
                        }

                        // Actividad actual
                        point.id == currentNextActivity -> {
                            createNumberedMarker(
                                context = mapView.context,
                                number = point.id
                            )
                        }

                        // Actividad bloqueada
                        else -> {
                            createNumberedMarker(
                                context = mapView.context,
                                number = point.id
                            )
                        }
                    }

                    marker.icon = BitmapDrawable(mapView.resources, markerBitmap)

                    /**
                     * Gestión de la interacción con el marcador.
                     */
                    marker.setOnMarkerClickListener { _, _ ->
                        /*
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
                                        message = introMessage + " $nextActivity",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                        */

                        // NUEVA LÓGICA (TEMPORAL): Navegar directamente
                        navController.navigate("startactivity/${point.id}")

                        true
                    }

                    mapView.overlays.add(marker)
                    Log.d("MapScreen", "Marcador ${point.id} agregado. Total de overlays: ${mapView.overlays.size}")
                }
                
                // Invalidar el mapa para que se redibuje
                mapView.invalidate()
                Log.d("MapScreen", "Mapa invalidado. Total de marcadores: ${mapView.overlays.size}")
            }
        }

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
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        )
    }
}

