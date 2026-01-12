package com.lucascamarero.didaktikapp.screens.activities

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.viewmodels.Game2ViewModel
import kotlin.math.roundToInt

@Composable
fun Activity2Screen(
    navController: NavController,
    viewModel: Game2ViewModel = hiltViewModel() // Inyección correcta del ViewModel
) {
    // ===================================================================
    // EVITA QUE GIRE HORIZONTALMENTE
    // ===================================================================
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        // Forzamos vertical al entrar
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            // Al salir de esta pantalla, permitimos que el sensor decida (vuelve a ser rotatorio)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    // --- UI PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // TÍTULO
        Text(
            text = "Quiz sobre la Iglesia de San Vicente (${viewModel.currentPhaseIndex + 1}/4)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        if (viewModel.isGameFinished) {
            // PANTALLA FINAL
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¡Juego Terminado!", fontSize = 28.sp, color = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { viewModel.restartGame() }) {
                        Text("REINICIAR JUEGO")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("SALIR AL MAPA")
                    }
                }
            }
        } else {

            // --- PARTE 1: IMAGEN (ZONA DE DROP) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f) // Ocupa el 60% del espacio disponible
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = viewModel.currentPhase.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp)
                    )

                    // Zona visual donde aparece la palabra soltada
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(if (viewModel.isCorrectAnswer) Color(0xAA4CAF50) else Color(0xDDFFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.droppedWord != null) {
                            Text(
                                text = viewModel.droppedWord!!,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        } else {
                            Text("Suelta aquí la palabra", color = Color.Gray)
                        }
                    }
                }
            }

            // --- PARTE 2: MENSAJE Y BOTONES ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f) // Ocupa el 40% del espacio
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Mensaje de feedback
                Text(
                    text = viewModel.feedbackMessage,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = if (viewModel.isCorrectAnswer) Color(0xFF2E7D32)
                    else if (viewModel.feedbackMessage.contains("Incorrecto")) Color.Red
                    else Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Lógica de visualización: Botón Siguiente o Palabras
                if (viewModel.isCorrectAnswer) {
                    Button(
                        onClick = { viewModel.nextPhase() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF154c79))
                    ) {
                        Text("SIGUIENTE")
                    }
                } else {
                    // Grid de opciones arrastrables
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Dividimos las opciones en filas de 2
                        val rows = viewModel.currentPhase.options.chunked(2)
                        rows.forEach { rowWords ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowWords.forEach { word ->
                                    DraggableOption(
                                        text = word,
                                        onDrop = { droppedText ->
                                            viewModel.checkAnswer(droppedText)
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

// --- COMPONENTE ARRASTRABLE ---
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
                        // DETECCIÓN DE CAÍDA: Si se arrastra hacia arriba (Y negativo)
                        // Ajusta -100 según la distancia necesaria en tu pantalla
                        if (offsetY < -100) {
                            onDrop(text)
                        }
                        // Efecto resorte: vuelve al sitio original
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