package com.lucascamarero.didaktikapp.screens.activities

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
// 1. IMPORTAMOS TU COMPONENTE
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity
import com.lucascamarero.didaktikapp.viewmodels.Game3ViewModel

// --- COLORES ---
val PrimaryBlue = Color(0xFF1A3B5D)
val FoundYellow = Color(0xFFFFC107)
val SelectionHighlight = Color(0xFFFFF176)
val BackgroundGray = Color(0xFFF0F2F5)
val SuccessGreen = Color(0xFF4CAF50)

@Composable
fun Activity3Screen(
    navController: NavController,
    viewModel: Game3ViewModel = hiltViewModel()
) {
    // ===================================================================
    // 1. CONFIGURACIÓN DE PANTALLA (BLOQUEO VERTICAL)
    // ===================================================================
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val personaId = 1
    val scrollState = rememberScrollState()
    val foundWordsList = viewModel.foundWords

    // Usamos Box para superponer el mensaje final
    Box(modifier = Modifier.fillMaxSize().background(BackgroundGray)) {

        // ===================================================================
        // CAPA 1: JUEGO (SOPA DE LETRAS) - SIEMPRE DE FONDO
        // ===================================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TÍTULO DE LA ACTIVIDAD
            Text(
                text = "Sopa de Letras: Sabores de Barakaldo",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                color = PrimaryBlue
            )

            // PANEL DE PISTAS
            WordSearchCluePanel(foundWordsList.toSet())
            Spacer(modifier = Modifier.height(20.dp))

            // SOPA DE LETRAS INTERACTIVA
            WordSearchGrid(viewModel, personaId)

            // Espacio extra al final para que se pueda hacer scroll cómodo
            Spacer(modifier = Modifier.height(60.dp))
        }

        // ===================================================================
        // CAPA 2: MENSAJE FINAL (POP-UP)
        // ===================================================================
        if (viewModel.isGameFinished) {
            MensajeFinalActivity(
                titulo = "¡SABROSO!",
                mensaje = "Has encontrado todos los productos típicos de la gastronomía local.",
                botonText = "VER RECOMPENSA",
                onButtonClick = {
                    // ID = 3
                    // Asegúrate de usar las fotos correctas para la Actividad 3
                    val ruta = "endactivity/3/${R.drawable.act1_premio1}/${R.drawable.act1_premio2}"
                    navController.navigate(ruta) {
                        popUpTo("activity3") { inclusive = true }
                    }
                }
            )
        }
    }
}


// -------------------------------------------------------------------
// 2. COMPONENTE DE LA CUADRÍCULA (SOPA DE LETRAS) - SIN CAMBIOS
// -------------------------------------------------------------------
@Composable
fun WordSearchGrid(viewModel: Game3ViewModel, personaId: Int) {
    val gridSize = 320.dp
    val foundWordsSet = viewModel.foundWords.toSet()

    Card(
        modifier = Modifier
            .size(gridSize)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = { viewModel.endSelection(personaId) },
                        onDrag = { change, _ ->
                            val sizePx = gridSize.toPx()
                            val row = (change.position.y / sizePx * 11).toInt()
                            val col = (change.position.x / sizePx * 11).toInt()

                            if (row in 0..10 && col in 0..10) {
                                viewModel.onCellTouch(row, col)
                            }
                        }
                    )
                }
        ) {
            viewModel.grid.forEachIndexed { r, rowStr ->
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    rowStr.forEachIndexed { c, char ->
                        val isSelected = viewModel.currentSelection.contains(r to c)
                        val isFound = viewModel.targetWords.any {
                            it.word in foundWordsSet &&
                                    r >= it.startRow && r <= it.endRow &&
                                    c >= it.startCol && c <= it.endCol
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    when {
                                        isFound -> FoundYellow.copy(alpha = 0.8f)
                                        isSelected -> SelectionHighlight.copy(alpha = 0.8f)
                                        else -> Color.White
                                    }
                                )
                                .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
            }
        }
    }
}


// -------------------------------------------------------------------
// 3. COMPONENTE DEL PANEL DE PISTAS - SIN CAMBIOS
// -------------------------------------------------------------------
@Composable
fun WordSearchCluePanel(foundWords: Set<String>) {
    val clues = remember {
        listOf(
            ClueData("Legumbre muy típica de Barakaldo, se come en guisos", "ALUBIAS"),
            ClueData("Se come en fiestas y se hace de maíz", "TALO"),
            ClueData("Pescado que se cocina al pil-pil", "BACALAO"),
            ClueData("Dulce típico de repostería de Barakaldo", "PASTEL")
        )
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.9f),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "PISTAS:",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = PrimaryBlue,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                clues.forEach { clue ->
                    ClueItemImproved(
                        text = clue.description,
                        word = clue.word,
                        completed = foundWords.contains(clue.word)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------
// 4. COMPONENTE INDIVIDUAL DE LA PISTA - SIN CAMBIOS
// -------------------------------------------------------------------
data class ClueData(val description: String, val word: String)

@Composable
fun ClueItemImproved(text: String, word: String, completed: Boolean) {
    val textColor = if (completed) Color.Gray else Color.Black

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (completed) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Encontrada",
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(14.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = if (completed) FontWeight.Normal else FontWeight.Medium
            ),
            color = textColor
        )
    }
}