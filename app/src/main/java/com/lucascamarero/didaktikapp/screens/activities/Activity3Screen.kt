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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity
import com.lucascamarero.didaktikapp.viewmodels.Game3ViewModel

/**
 * Pantalla principal de la Actividad 3.
 *
 * Contiene:
 * - El título de la actividad
 * - El panel de pistas
 * - La sopa de letras interactiva
 * - El mensaje final cuando el juego se completa
 *
 * @param navController Controlador de navegación
 * @param viewModel ViewModel que gestiona el estado y la lógica del juego
 */
@Composable
fun Activity3Screen(
    navController: NavController,
    viewModel: Game3ViewModel = hiltViewModel()
) {
    /** Identificador del usuario que realiza la actividad */
    val personaId = 1

    /** Estado del scroll vertical de la pantalla */
    val scrollState = rememberScrollState()

    /** Lista reactiva de palabras encontradas */
    val foundWordsList = viewModel.foundWords

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

        // CAPA 1: JUEGO (SOPA DE LETRAS)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título de la actividad
            Text(
                text = stringResource(id = R.string.texto47),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Panel de pistas del juego
            WordSearchCluePanel(foundWordsList.toSet())
            Spacer(modifier = Modifier.height(20.dp))

            //Sopa de letras interactiva
            WordSearchGrid(viewModel, personaId)

            Spacer(modifier = Modifier.height(60.dp))
        }

        // Mensaje final mostrado cuando el juego se completa
        if (viewModel.isGameFinished) {
            MensajeFinalActivity(
                titulo = stringResource(id = R.string.texto48),
                mensaje = stringResource(id = R.string.texto49),
                botonText = stringResource(id = R.string.texto50),
                onButtonClick = {
                    val ruta =
                        "endactivity/3/${R.drawable.premio31}/${R.drawable.premio12}"
                    navController.navigate(ruta) {
                        popUpTo("activity3") { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Composable que representa la sopa de letras.
 *
 * Gestiona:
 * - La detección de gestos de arrastre
 * - El marcado de letras seleccionadas
 * - El resaltado de palabras encontradas
 *
 * @param viewModel ViewModel que contiene el estado del juego
 * @param personaId Identificador del usuario
 */
@Composable
fun WordSearchGrid(viewModel: Game3ViewModel, personaId: Int) {

    /** Tamaño fijo del grid */
    val gridSize = 320.dp

    /** Conjunto de palabras ya encontradas */
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
                .background(MaterialTheme.colorScheme.background)
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

                        /** Indica si la celda está seleccionada actualmente */
                        val isSelected = viewModel.currentSelection.contains(r to c)

                        /** Indica si la celda pertenece a una palabra ya encontrada */
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
                                        isFound -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                                        isSelected -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                                        else -> MaterialTheme.colorScheme.background
                                    }
                                )
                                .border(
                                    0.5.dp,
                                    MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Panel que muestra las pistas del juego y su estado
 * (pendiente o completada).
 *
 * @param foundWords Conjunto de palabras ya encontradas
 */
@Composable
fun WordSearchCluePanel(foundWords: Set<String>) {

    /**
     * Lista de pistas y respuestas asociadas
     */
    val clues = listOf(
        ClueData(stringResource(id = R.string.pista3_1), stringResource(id = R.string.respuesta3_1)),
        ClueData(stringResource(id = R.string.pista3_2), stringResource(id = R.string.respuesta3_2)),
        ClueData(stringResource(id = R.string.pista3_3), stringResource(id = R.string.respuesta3_3)),
        ClueData(stringResource(id = R.string.pista3_4), stringResource(id = R.string.respuesta3_4)),
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.9f),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título del panel de pistas
            Text(
                text = stringResource(id = R.string.texto51),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onPrimary,
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

/**
 * Modelo que representa una pista y su palabra asociada.
 *
 * @property description Texto descriptivo de la pista
 * @property word Palabra solución asociada
 */
data class ClueData(val description: String, val word: String)

/**
 * Elemento visual que representa una pista individual.
 *
 * Muestra:
 * - Un icono de check si la pista está completada
 * - Un indicador circular si está pendiente
 * - El texto de la pista con estilos según su estado
 *
 * @param text Texto descriptivo de la pista
 * @param word Palabra asociada
 * @param completed Indica si la pista ya ha sido resuelta
 */
@Composable
fun ClueItemImproved(text: String, word: String, completed: Boolean) {

    /** Color del texto según el estado de la pista */
    val textColor =
        if (completed) MaterialTheme.colorScheme.scrim
        else MaterialTheme.colorScheme.tertiary

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (completed) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Encontrada",
                tint = MaterialTheme.colorScheme.scrim,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onTertiaryContainer)
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