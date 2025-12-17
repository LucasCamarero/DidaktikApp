package com.lucascamarero.didaktikapp.screens.activities

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
data class PhaseData(
    val id: Int,
    val imageRes: Int,
    val correctWord: String,
    val options: List<String>
)

@Composable
fun Activity2Screen(
    navController: NavController? = null
) {
    // --- 1. CONFIGURACIÓN DE FASES ---
    val phases = remember {
        listOf(
            PhaseData(1, R.drawable.activ2_img_altar, "Altar", listOf("Altar", "Coro", "Sagrario", "Vía Crucis").shuffled()),
            PhaseData(2, R.drawable.activ2_img_coro, "Coro", listOf("Púlpito", "Coro", "Banco", "Confesionario").shuffled()),
            PhaseData(3, R.drawable.activ2_img_sagrario, "Sagrario", listOf("Sagrario", "Cáliz", "Vela", "Altar").shuffled()),
            PhaseData(4, R.drawable.activ2_img_via_crucis, "Vía Crucis", listOf("Cruz", "Vía Crucis", "Cuadro", "Estatua").shuffled())
        )
    }

    // --- ESTADOS ---
    var currentPhaseIndex by remember { mutableStateOf(0) }
    val currentPhase = phases[currentPhaseIndex]
    var droppedWord by remember { mutableStateOf<String?>(null) }
    var feedbackMessage by remember { mutableStateOf("Arrastra la palabra correcta a la imagen.") }
    var isCorrectAnswer by remember { mutableStateOf(false) }
    var isGameFinished by remember { mutableStateOf(false) }
    var dropZoneRect by remember { mutableStateOf(Rect.Zero) }

    // --- UI PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // TÍTULO (Altura fija, no entra en el reparto de weights)
        Text(
            text = "Quiz sobre la Iglesia de San Vicente (${currentPhaseIndex + 1}/4)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isGameFinished) {
            // PANTALLA FINAL
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¡Juego Terminado!", fontSize = 28.sp, color = Color(0xFF2E7D32))
                    Button(onClick = {
                        currentPhaseIndex = 0; droppedWord = null; isGameFinished = false; isCorrectAnswer = false
                    }) { Text("REINICIAR") }
                }
            }
        } else {

            // --- PARTE 1: IMAGEN (2/4 de la altura = weight 2f) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f) // <--- AQUÍ ESTÁ EL AJUSTE DEL 50% SUPERIOR
                    .padding(vertical = 8.dp)
                    .onGloballyPositioned { coordinates ->
                        dropZoneRect = coordinates.boundsInWindow()
                    },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = currentPhase.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)
                    )

                    // Zona de texto soltado
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(if (isCorrectAnswer) Color(0xAA4CAF50) else Color(0xAAFFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (droppedWord != null) {
                            Text(droppedWord!!, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Suelta aquí", color = Color.Gray)
                        }
                    }
                }
            }

            // --- PARTE 2: MENSAJE Y BOTONES (2/4 de la altura = weight 2f) ---
            // Agrupamos todo esto en una Columna para que ocupe la otra mitad exacta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f) // <--- AQUÍ ESTÁ EL AJUSTE DEL 50% INFERIOR
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.Center, // Centramos verticalmente el contenido
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Mensaje
                Text(
                    text = feedbackMessage,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = if (isCorrectAnswer) Color(0xFF2E7D32) else if (feedbackMessage.contains("incorrecto", true)) Color.Red else Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Botones / Opciones
                if (isCorrectAnswer) {
                    Button(
                        onClick = {
                            if (currentPhaseIndex < phases.size - 1) {
                                currentPhaseIndex++
                                droppedWord = null
                                isCorrectAnswer = false
                                feedbackMessage = "Arrastra la palabra correcta."
                            } else {
                                isGameFinished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF154c79))
                    ) {
                        Text(if (currentPhaseIndex < phases.size - 1) "SIGUIENTE" else "FINALIZAR")
                    }
                } else {
                    // Grid de opciones
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val rows = currentPhase.options.chunked(2)
                        rows.forEach { rowWords ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowWords.forEach { word ->
                                    DraggableOption(
                                        text = word,
                                        onDrop = { droppedText ->
                                            if (droppedText == currentPhase.correctWord) {
                                                droppedWord = droppedText
                                                isCorrectAnswer = true
                                                feedbackMessage = "¡Correcto!"
                                            } else {
                                                feedbackMessage = "¡Incorrecto! Inténtalo de nuevo."
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTE ARRASTRABLE (Simplificado para validación visual vertical) ---
@Composable
fun DraggableOption(
    text: String,
    onDrop: (String) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .zIndex(if (isDragging) 10f else 1f)
            .graphicsLayer {
                scaleX = if (isDragging) 1.1f else 1f
                scaleY = if (isDragging) 1.1f else 1f
                shadowElevation = if (isDragging) 8.dp.toPx() else 0f
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        // DETECCIÓN DE CAÍDA (Simplificada: Si arrastró mucho hacia arriba)
                        // Como la imagen ocupa la mitad superior, un offset Y negativo grande indica intención
                        if (offsetY < -150) {
                            onDrop(text)
                        }
                        // Efecto resorte: vuelve al sitio siempre
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun Activity2ScreenPreview() {
    Activity2Screen()
}