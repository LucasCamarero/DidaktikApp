package com.lucascamarero.didaktikapp.screens.activities

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CustomGameButton
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
    val personaId = 1
    val scrollState = rememberScrollState()

    // Importante: Requerimos el estado de foundWords en Compose para que se actualice la UI
    // Aunque es una MutableStateList, la usamos para que Compose sepa cuándo redibujar.
    // Al accederla, Compose detecta el cambio.
    val foundWordsList = viewModel.foundWords

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
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

        // PANEL DE PISTAS MEJORADO
        WordSearchCluePanel(foundWordsList.toSet())
        Spacer(modifier = Modifier.height(40.dp))

        // SOPA DE LETRAS INTERACTIVA
        WordSearchGrid(viewModel, personaId)

        // BOTÓN DE COMPLETADO
        if (viewModel.isGameFinished) {
            Spacer(modifier = Modifier.height(24.dp))

            CustomGameButton(
                text = "¡NIVEL COMPLETADO!",
                backgroundResId = R.drawable.boton_azul,
                onClick = {
                    navController.navigate("map")
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 16.dp)
                    .height(68.dp),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}


// -------------------------------------------------------------------
// 2. COMPONENTE DE LA CUADRÍCULA (SOPA DE LETRAS)
// -------------------------------------------------------------------
@Composable
fun WordSearchGrid(viewModel: Game3ViewModel, personaId: Int) {
    val gridSize = 320.dp
    // Usamos viewModel.foundWords directamente para que Compose reaccione a los cambios
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

                        // 💡 CORREGIDO: Uso de it.startRow y foundWordsSet
                        val isFound = viewModel.targetWords.any {
                            it.word in foundWordsSet &&
                                    r >= it.startRow && r <= it.endRow && // Soporte para búsqueda vertical
                                    c >= it.startCol && c <= it.endCol // Soporte para búsqueda horizontal
                        }

                        Box( // 💡 CORREGIDO: Este Box debe estar dentro del bucle forEachIndexed(c)
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
                    } // Fin del forEachIndexed (c)
                }
            }
        }
    }
}


// -------------------------------------------------------------------
// 3. COMPONENTE DEL PANEL DE PISTAS (MEJORADO)
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
            // Título de la sección de PISTAS
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
// 4. COMPONENTE INDIVIDUAL DE LA PISTA (MEJORADO)
// -------------------------------------------------------------------
data class ClueData(val description: String, val word: String)

@Composable
fun ClueItemImproved(text: String, word: String, completed: Boolean) {
    val textColor = if (completed) Color.Gray else Color.Black

    Row(verticalAlignment = Alignment.CenterVertically) {
        // 1. Indicador Visual (Icono o Punto)
        if (completed) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Encontrada",
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Punto gris simple si no se ha encontrado
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(14.dp)) // Espacio para alinear con el icono de Check
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 2. Texto de la Pista
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