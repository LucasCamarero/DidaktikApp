package com.lucascamarero.didaktikapp.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucascamarero.didaktikapp.models.MapPoint

@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    val key : String = getMapsApiKey(context)
    Log.d("pruebas","la key es::"+key)

    // Datos hardcodeados de momento
    val mapPoints = listOf(
        MapPoint(1, 43.257611, -2.979528, "Ermita Santa Águeda"),
        MapPoint(2, 43.295750, -2.996722, "Iglesia de San Vicente"),
        MapPoint(3, 43.296902, -2.987188, "Mercado de Abastos"),
        MapPoint(4, 43.302592, -2.985833, "Edificio Ilgner"),
        MapPoint(5, 43.315600, -3.010764, "Cargadero de minas"),
        MapPoint(6, 43.305550, -2.982158, "Ferrocarril"),
        MapPoint(7, 43.295125, -2.978303, "Palacio Munoa")
    )

    // Cámara centrada en la zona de los puntos
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(43.30, -2.99),
            14f
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL
        )
    ) {
        mapPoints.forEach { point ->
            Marker(
                state = MarkerState(
                    position = LatLng(point.lat, point.lng)
                ),
                title = point.name,
                snippet = "Punto ${point.id}",
                onClick = {
                    true
                }
            )
        }
    }
}

fun getMapsApiKey(context: Context): String {
    val resultado : String?
    try {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val bundle = appInfo.metaData
        resultado =  bundle.getString("com.google.android.geo.API_KEY")
    } catch (e: Exception) {
        return "Error"
    }
    if(resultado==null) {
        return "Error"
    } else {
        return resultado
    }
}