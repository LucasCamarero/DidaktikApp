package com.lucascamarero.didaktikapp.screens.activities

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity
import com.lucascamarero.didaktikapp.viewmodels.GameViewModel
import kotlin.math.roundToInt

@Composable
fun Activity1Screen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    // ===================================================================
    // 1. BLOQUEO DE ROTACIÓN (Solo Vertical)
    // ===================================================================
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5))) {


        // ===================================================================
        // ESCENARIO 1: JUEGO (Drag & Drop + Quiz)
        // ===================================================================

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // IMAGEN ESCENARIO
            Image(
                painter = painterResource(id = R.drawable.act1_bg_game2),
                contentDescription = "Escenario",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 0.dp)
                    .weight(2f)
                    .clip(RoundedCornerShape(12.dp))
            )

            // TEXTO DE ESTADO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.statusText,
                    color = viewModel.statusColor,
                    fontSize = if (viewModel.isQuizMode) 18.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // ZONA DINÁMICA (Cajas para soltar o Preguntas Quiz)
            if (!viewModel.isQuizMode) {
                // MODO DRAG & DROP
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(0.5f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.items.forEach { _ ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0E0E0))
                        )
                    }
                }
            } else {
                // MODO QUIZ
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizOption(text = "a) La ermita sigue en pie", id = 1, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 1 }
                    QuizOption(text = "b) Ya no hay montes", id = 2, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 2 }
                    QuizOption(text = "c) No hay fiestas", id = 3, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 3 }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE ACCIÓN
            Button(
                onClick = { viewModel.onMainButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.isDragSuccess && !viewModel.isQuizMode) Color(0xFF4CAF50) else Color(0xFF0088CC)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                val buttonText = when {
                    !viewModel.isDragSuccess -> "COMPROBAR RESPUESTA"
                    !viewModel.isQuizMode -> "CONTINUAR"
                    else -> "COMPROBAR RESPUESTA"
                }
                Text(text = buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // ELEMENTOS ARRASTRABLES (SOLO VISIBLES EN MODO DRAG & DROP)
        if (!viewModel.isQuizMode) {
            // 1. DIBUJAR LOS TARGETS
            viewModel.items.forEach { item ->
                Box(
                    modifier = Modifier
                        .offset { IntOffset(item.targetPosition.x.roundToInt(), item.targetPosition.y.roundToInt()) }
                        .size(90.dp)
                        .border(3.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        .background(Color.Green.copy(alpha = 0.2f), CircleShape)
                )
            }

            // 2. DIBUJAR LOS OBJETOS MOVIBLES
            viewModel.items.forEach { item ->
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Objeto",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset { IntOffset(item.currentPosition.x.roundToInt(), item.currentPosition.y.roundToInt()) }
                        .size(90.dp)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                viewModel.updateItemPosition(item.id, dragAmount)
                            }
                        }
                )
            }
        }


        // ===================================================================
        // ESCENARIO 2: JUEGO TERMINADO -> MENSAJE DE ÉXITO
        // ===================================================================
        if (viewModel.isRewardUnlocked) {
            // 2. USAMOS EL COMPONENTE REUTILIZABLE
            MensajeFinalActivity(
                titulo = "¡MUY BIEN!",
                mensaje = "Has completado la actividad de la Ermita de Santa Águeda correctamente.",
                botonText = "VER RECOMPENSA", // Texto sugerente para ir a las fotos
                onButtonClick = {
                    // 3. NAVEGAMOS A LA RUTA GENÉRICA CON LAS FOTOS DE LA ACTIVIDAD 1
                    val ruta = "endactivity/1/${R.drawable.act1_premio1}/${R.drawable.act1_premio2}"
                    navController.navigate(ruta) {
                        // Evita volver al juego si pulsas atrás desde la pantalla final
                        popUpTo("activity1") { inclusive = true }
                    }
                }
            )
        }
    }
}

// --- COMPONENTES AUXILIARES ---
@Composable
fun QuizOption(text: String, id: Int, selectedId: Int, onSelect: () -> Unit) {
    val isSelected = (id == selectedId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFBBDEFB) else Color.White)
            .border(1.dp, if (isSelected) Color(0xFF0088CC) else Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0088CC))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}