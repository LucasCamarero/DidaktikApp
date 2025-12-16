package com.lucascamarero.didaktikapp.screens.activities

import android.R.attr.translationZ
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import kotlin.math.roundToInt

// --- MODELOS DE DATOS ---

// Representa una de las 4 zonas (Imágenes de la iglesia)
data class DropZone(
    val id: Int,
    val title: String, // "Altar", "Coro", etc. (Lo usamos para validar internamente)
    val imageRes: Int
)

// Representa la etiqueta que arrastramos
data class DraggableWord(
    val id: Int,
    val text: String,
    val correctZoneId: Int // Con qué zona debe emparejarse
)

@Composable
fun Activity2Screen(
    navController: NavController? = null
) {
    // --- DATOS INICIALES ---
    // DEFINE AQUÍ TUS IMÁGENES REALES DE LA IGLESIA
    val zones = remember {
        listOf(
            DropZone(1, "Altar", R.drawable.activ2_img_altar),      // Cambiar por R.drawable.img_altar
            DropZone(2, "Coro", R.drawable.activ2_img_coro),       // Cambiar por R.drawable.img_coro
            DropZone(3, "Sagrario", R.drawable.activ2_img_sagrario),   // Cambiar por R.drawable.img_sagrario
            DropZone(4, "Vía Crucis", R.drawable.activ2_img_via_crucis)  // Cambiar por R.drawable.img_viacrucis
        )
    }

    // Las palabras a arrastrar
    val words = remember {
        listOf(
            DraggableWord(1, "Altar", 1),
            DraggableWord(2, "Coro", 2),
            DraggableWord(3, "Sagrario", 3),
            DraggableWord(4, "Vía Crucis", 4)
        ).shuffled() // Las mezclamos para que no salgan en orden
    }

    // --- ESTADO DEL JUEGO ---

    // Guarda dónde está cada palabra actualmente: Map<WordId, ZoneId?>
    // Si el valor es null, la palabra está en la "piscina" de abajo.
    var wordPlacements by remember { mutableStateOf(words.associate { it.id to (null as Int?) }) }

    // Guarda las coordenadas en pantalla de cada zona de caída (para detectar colisiones)
    val zoneBounds = remember { mutableStateMapOf<Int, Rect>() }

    // Mensajes de feedback
    var message by remember { mutableStateOf("Arrastra las palabras a su imagen correspondiente.") }
    var isSuccess by remember { mutableStateOf(false) }

    // Offset temporal mientras arrastramos (x, y)
    var currentDragOffset by remember { mutableStateOf(Offset.Zero) }
    // Qué palabra estamos arrastrando ahora mismo
    var currentlyDraggingWordId by remember { mutableStateOf<Int?>(null) }


    // --- UI PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Iglesia de San Vicente",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- ZONA SUPERIOR: GRID DE IMÁGENES (Drop Zones) ---
        // Usamos un Box para poder superponer las etiquetas "soltadas" encima de las imágenes
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Fila 1 (Zonas 1 y 2)
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DropZoneItem(zones[0], zoneBounds, wordPlacements, words)
                    DropZoneItem(zones[1], zoneBounds, wordPlacements, words)
                }
                // Fila 2 (Zonas 3 y 4)
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DropZoneItem(zones[2], zoneBounds, wordPlacements, words)
                    DropZoneItem(zones[3], zoneBounds, wordPlacements, words)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ZONA MEDIA: MENSAJE ---
        Text(
            text = message,
            fontSize = 16.sp,
            color = if (message.contains("¡Correcto!", true)) Color(0xFF2E7D32) else if (message.contains("incorrecto", true)) Color.Red else Color.Black,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        // --- ZONA INFERIOR: PALABRAS ARRASTRABLES (Pool inicial) ---
        // Aquí mostramos solo las palabras que NO han sido colocadas en ninguna zona (value == null)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Altura fija para el área de palabras
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            words.forEach { word ->
                // Solo renderizamos aquí si NO está colocada en una zona
                if (wordPlacements[word.id] == null) {
                    DraggableWordTag(
                        word = word,
                        isDragging = currentlyDraggingWordId == word.id,
                        dragOffset = if (currentlyDraggingWordId == word.id) currentDragOffset else Offset.Zero,
                        onDragStart = { currentlyDraggingWordId = word.id },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            currentDragOffset += dragAmount
                        },
                        onDragEnd = {
                            // Lógica mágica: ¿Dónde solté la palabra?
                            // Buscamos si el gesto terminó dentro de algún rectángulo de zona
                            // Nota: Necesitamos convertir la posición del gesto a coordenadas globales,
                            // pero para simplificar en este ejemplo, usaremos la detección visual aproximada.
                            // Una forma robusta es verificar bounds.

                            // Iteramos las zonas para ver si el usuario soltó encima de alguna
                            // (Esto requiere lógica compleja de coordenadas globales vs locales)

                            // *** SOLUCIÓN SIMPLIFICADA PARA UN SOLO FICHERO ***
                            // Vamos a usar un truco: En onDragEnd no tenemos las coordenadas globales del evento fácilmente sin modifiers extra.
                            // Pero como hemos actualizado 'currentDragOffset', podemos estimar o usar un DropTarget.

                            // Para mantenerlo simple y funcional:
                            // Vamos a detectar la colisión basándonos en dónde está el dedo RELATIVO a la pantalla.
                            // Como esto es complejo de calcular aquí sin pasar el contexto global,
                            // haremos que al soltar, busquemos la zona más cercana o intersectada.

                            // REVISIÓN: La mejor forma en un solo fichero es comprobar colisión manual
                            // usando las coordenadas globales que guardamos en 'zoneBounds'.

                            // Pero necesitamos saber las coordenadas globales ACTUALES de la palabra arrastrada.
                            // Eso es difícil sin un estado global de puntero.

                            // ALTERNATIVA DE ARRASTRE:
                            // Resetear siempre al soltar si no es válido.
                            // Para validar la caída, necesitamos pasar las coordenadas del puntero.
                        },
                        // Pasamos el mapa de zonas para calcular la caída dentro del componente
                        zonesBounds = zoneBounds,
                        onDropInZone = { zoneId ->
                            // Asignamos la palabra a la zona
                            val newMap = wordPlacements.toMutableMap()
                            // Si esa zona ya tenía algo, lo devolvemos a la piscina (opcional, o reemplazamos)
                            // Aquí permitimos reemplazar: buscamos si alguien ya tiene este zoneId
                            val previousOwner = newMap.entries.find { it.value == zoneId }?.key
                            if (previousOwner != null) {
                                newMap[previousOwner] = null
                            }
                            newMap[word.id] = zoneId
                            wordPlacements = newMap

                            currentlyDraggingWordId = null
                            currentDragOffset = Offset.Zero
                        },
                        onDropCancel = {
                            currentlyDraggingWordId = null
                            currentDragOffset = Offset.Zero
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- BOTÓN VALIDAR ---
        Button(
            onClick = {
                // Validar lógica
                var allCorrect = true
                var placedCount = 0

                wordPlacements.forEach { (wordId, zoneId) ->
                    if (zoneId != null) {
                        placedCount++
                        val word = words.find { it.id == wordId }
                        if (word != null && word.correctZoneId != zoneId) {
                            allCorrect = false
                        }
                    } else {
                        // Si hay alguna sin colocar, no está completo (opcional, o cuenta como error)
                        allCorrect = false
                    }
                }

                if (placedCount < 4) {
                    message = "Faltan palabras por colocar."
                    isSuccess = false
                } else if (allCorrect) {
                    message = "¡Correcto! Has identificado todos los elementos."
                    isSuccess = true
                } else {
                    message = "Algún elemento es incorrecto. Inténtalo de nuevo."
                    isSuccess = false
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF154c79))
        ) {
            Text("VALIDAR", fontSize = 18.sp, color = Color.White)
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun RowScope.DropZoneItem(
    zone: DropZone,
    zoneBounds: MutableMap<Int, Rect>,
    wordPlacements: Map<Int, Int?>,
    allWords: List<DraggableWord>
) {
    // Buscamos qué palabra está asignada a esta zona (si hay alguna)
    val assignedWordId = wordPlacements.entries.find { it.value == zone.id }?.key
    val assignedWord = allWords.find { it.id == assignedWordId }

    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                // Mágia: Guardamos las coordenadas globales de esta tarjeta
                zoneBounds[zone.id] = coordinates.boundsInWindow()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. La Imagen de fondo
            Image(
                painter = painterResource(id = zone.imageRes),
                contentDescription = zone.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().padding(bottom = 30.dp) // Dejamos espacio abajo para la etiqueta
            )

            // 2. Área para la etiqueta soltada
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xAAFFFFFF)), // Fondo semitransparente
                contentAlignment = Alignment.Center
            ) {
                if (assignedWord != null) {
                    // Si hay palabra asignada, mostramos una "copia" estática de la etiqueta aquí
                    // Y permitimos que se pueda "sacar" (clic para devolver a la piscina)
                    DraggableWordTagStatic(
                        text = assignedWord.text,
                        // Opcional: Permitir arrastrar desde aquí también requeriría más lógica.
                        // Para simplificar, si tocas una colocada, la dejas ahí o usas lógica de reset.
                        // En este diseño simple, si quieres moverla, la lógica de abajo la resetea si arrastras otra encima.
                    )
                } else {
                    Text("Arrastra aquí", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

// Etiqueta que se mueve
// Etiqueta que se mueve
@Composable
fun DraggableWordTag(
    word: DraggableWord,
    isDragging: Boolean,
    dragOffset: Offset,
    onDragStart: () -> Unit,
    onDrag: (androidx.compose.ui.input.pointer.PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit,
    zonesBounds: Map<Int, Rect>,
    onDropInZone: (Int) -> Unit,
    onDropCancel: () -> Unit
) {
    // Obtenemos densidad para cálculos
    val density = LocalDensity.current

    // Variable para guardar nuestra propia posición global mientras nos movemos
    var myGlobalPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .offset {
                // Aplicamos el movimiento visual
                IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())
            }
            .graphicsLayer {
                // Si arrastramos, aumentamos escala y sombra
                scaleX = if (isDragging) 1.2f else 1f
                scaleY = if (isDragging) 1.2f else 1f
                shadowElevation = if (isDragging) 10.dp.toPx() else 0f
                // ELIMINADA LA LÍNEA DE translationZ QUE DABA ERROR
            }
            // Esto es lo que realmente lo pone por encima de todo (z-index)
            .zIndex(if (isDragging) 100f else 1f)
            .onGloballyPositioned {
                // Guardamos dónde estamos en la pantalla
                myGlobalPosition = it.boundsInWindow().center
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = {
                        // LÓGICA DE COLISIÓN MANUAL
                        var droppedZoneId: Int? = null

                        // Revisamos si el centro de la etiqueta cae dentro de alguna zona
                        for ((id, rect) in zonesBounds) {
                            if (rect.contains(myGlobalPosition)) {
                                droppedZoneId = id
                                break
                            }
                        }

                        if (droppedZoneId != null) {
                            onDropInZone(droppedZoneId)
                        } else {
                            onDropCancel()
                        }
                        onDragEnd()
                    },
                    onDragCancel = { onDropCancel() },
                    onDrag = onDrag
                )
            }
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = word.text, fontWeight = FontWeight.Bold)
    }
}

// Etiqueta visual estática (cuando ya está colocada en la imagen)
@Composable
fun DraggableWordTagStatic(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFEB3B), RoundedCornerShape(8.dp)) // Amarillo para destacar colocada
            .border(1.dp, Color(0xFFFBC02D), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun Activity2ScreenPreview() {
    Activity2Screen()
}