package com.lucascamarero.didaktikapp.screens.commontoactivities.minijuegos


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * @param resourceId ID del recurso de la imagen (ej: R.drawable.vaca)
 * @param onDrop Función lambda que se llama cuando se suelta el objeto.
 * Recibe la posición final (Offset) y el ID del objeto.
 */
@Composable
fun DraggableItem(
    resourceId: Int,
    itemId: String, // 'vaca', 'campesino', etc.
    initialOffset: Offset,
    onDrop: (Offset, String) -> Unit
) {
    // 1. ESTADO: Almacena la posición actual de la imagen.
    var currentOffset by remember { mutableStateOf(initialOffset) }

    Box(
        modifier = Modifier
            // 2. MODIFICADOR DE GESTOS: Detecta el arrastre
            .offset { IntOffset(currentOffset.x.roundToInt(), currentOffset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { /* Opcional: Feedback visual al inicio */ },
                    onDragEnd = {
                        // 3. EVENTO DE SOLTADO: Llama a la función de comprobación
                        onDrop(currentOffset, itemId)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    // Actualiza la posición de la imagen
                    currentOffset = currentOffset.plus(dragAmount)
                }
            }
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = itemId,
            // Aquí puedes definir el tamaño de la imagen
            // modifier = Modifier.size(60.dp)
        )
    }
}