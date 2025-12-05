package com.lucascamarero.didaktikapp.screens.commontoactivities.minijuegos


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.lucascamarero.didaktikapp.R // Asegúrate de que esta ruta es correcta

@Composable
fun Actividad1Screen(
    // Deberías inyectar el ViewModel aquí para manejar la lógica de datos
    // viewModel: RutaViewModel = hiltViewModel()
) {
    // Definimos las 'Drop Zones' (Zonas de Soltar).
    // NOTA: Estas coordenadas son ABSOLUTAS y debes calcularlas
    // manualmente en base al tamaño de la pantalla y la imagen de fondo.
    val dropZones = mapOf(
        "monte" to Rect(0f, 0f, 400f, 300f),       // Parte superior de la pantalla
        "cruz" to Rect(200f, 300f, 350f, 450f),   // Campana de la ermita
        "campesino" to Rect(100f, 500f, 300f, 650f), // Camino de la ermita
        "vaca" to Rect(350f, 600f, 600f, 800f)      // Pasto
    )

    // Función para manejar el soltado del objeto
    val handleDrop: (Offset, String) -> Unit = { finalOffset, itemId ->
        val correctZone = dropZones[itemId]
        if (correctZone != null && finalOffset.x in correctZone.left..correctZone.right && finalOffset.y in correctZone.top..correctZone.bottom) {
            // Lógica de éxito: Marcar el objeto como colocado, actualizar ViewModel
            println("$itemId SOLTADO CORRECTAMENTE en $finalOffset")
        } else {
            // Lógica de error/fallo: El objeto vuelve a su posición inicial o se queda flotando
            println("$itemId Soltado INCORRECTAMENTE en $finalOffset")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. FONDO (Imagen de la Ermita)
        Image(
            painter = painterResource(id = R.drawable.activ1_bg_game), // Cambia a tu recurso de fondo
            contentDescription = "Fondo de la Ermita Santa Águeda",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. ELEMENTOS ARRASTRABLES (Se colocan con un 'initialOffset' en la parte izquierda)
        // Nota: Los offsets iniciales son para colocar los objetos fuera o a un lado del fondo.
        DraggableItem(
            resourceId = R.drawable.activ1_vaca, // Reemplaza con tu recurso de vaca
            itemId = "vaca",
            initialOffset = Offset(20f, 400f), // Posición inicial
            onDrop = handleDrop
        )

        DraggableItem(
            resourceId = R.drawable.activ1_campesino, // Reemplaza con tu recurso de campesino
            itemId = "campesino",
            initialOffset = Offset(20f, 500f),
            onDrop = handleDrop
        )

        DraggableItem(
            resourceId = R.drawable.activ1_cruz, // Reemplaza con tu recurso de campesino
            itemId = "cruz",
            initialOffset = Offset(20f, 500f),
            onDrop = handleDrop
        )

        DraggableItem(
            resourceId = R.drawable.activ1_montania, // Reemplaza con tu recurso de campesino
            itemId = "montaña",
            initialOffset = Offset(20f, 500f),
            onDrop = handleDrop
        )

        // ... Agrega los otros DraggableItem (cruz, monte)

        // 3. (Opcional) UI para la pregunta tipo test
        // ... Solo se muestra si todos los objetos han sido colocados correctamente
    }
}

// Clase de utilidad simple para definir las zonas de soltar
data class Rect(val left: Float, val top: Float, val right: Float, val bottom: Float)