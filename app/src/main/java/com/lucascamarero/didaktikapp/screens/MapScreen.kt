package com.lucascamarero.didaktikapp.screens

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.components.MapPoint
import com.lucascamarero.didaktikapp.utils.createNumberedMarker
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(navController: NavController){

    val mapPoints = listOf(
        MapPoint(1, 43.257611, -2.979528, "Ermita Santa Águeda"),
        MapPoint(2, 43.295750, -2.996722, "Iglesia de San Vicente"),
        MapPoint(3, 43.296902, -2.987188, "Mercado de Abastos"),
        MapPoint(4, 43.302592, -2.985833, "Edificio Ilgner"),
        MapPoint(5, 43.315600, -3.010764, "Cargadero de minas"),
        MapPoint(6, 43.305550, -2.982158, "Ferrocarril"),
        MapPoint(7, 43.295125, -2.978303, "Palacio Munoa")
    )

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {

                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                controller.setZoom(16.0)
                controller.setCenter(
                    GeoPoint(43.30, -2.99)
                )

                mapPoints.forEach { point ->

                    val marker = Marker(this)
                    marker.position = GeoPoint(point.lat, point.lng)
                    marker.title = point.name
                    marker.subDescription = "Punto ${point.id}"

                    // 👉 ICONO NUMERADO
                    marker.icon = BitmapDrawable(
                        resources,
                        createNumberedMarker(context, point.id)
                    )

                    marker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )

                    marker.setOnMarkerClickListener { _, _ ->

                        navController.navigate("startactivity/${point.id}") {
                            launchSingleTop = true
                        }

                        true
                    }

                    overlays.add(marker)
                }
            }
        }
    )
}