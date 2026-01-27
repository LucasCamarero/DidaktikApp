package com.lucascamarero.didaktikapp.screens.activities

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

/**
 * Pantalla principal de la Actividad 1.
 *
 * Esta pantalla gestiona toda la interfaz del juego:
 * - Escenario visual
 * - Ejercicio de drag & drop
 * - Modo quiz
 * - Botón de acción principal
 * - Mensaje final de recompensa
 *
 * El estado y la lógica se delegan al [GameViewModel].
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param viewModel ViewModel que gestiona el estado del juego.
 */
@Composable
fun Activity1Screen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ESCENARIO 1: JUEGO (Drag & Drop + Quiz)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen principal del escenario del juego.
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

            // Contenedor del texto informativo del estado del juego.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = viewModel.statusTextResId),
                    color = viewModel.statusColor,
                    fontSize = if (viewModel.isQuizMode) 18.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            /*
             * Zona dinámica que alterna entre:
             * - Área de cajas para soltar (drag & drop)
             * - Opciones del quiz
             */
            if (!viewModel.isQuizMode) {
                // Modo cajas para soltar
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
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            } else {
                // Modo quiz
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizOption(
                        textResId = R.string.texto19,
                        id = 1,
                        selectedId = viewModel.selectedQuizOption
                    ) { viewModel.selectedQuizOption = 1 }

                    QuizOption(
                        textResId = R.string.texto20,
                        id = 2,
                        selectedId = viewModel.selectedQuizOption
                    ) { viewModel.selectedQuizOption = 2 }

                    QuizOption(
                        textResId = R.string.texto21,
                        id = 3,
                        selectedId = viewModel.selectedQuizOption
                    ) { viewModel.selectedQuizOption = 3 }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón principal de acción.
            Button(
                onClick = { viewModel.onMainButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (viewModel.isDragSuccess && !viewModel.isQuizMode)
                            MaterialTheme.colorScheme.scrim
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                val buttonTextResId = when {
                    !viewModel.isDragSuccess -> R.string.texto17
                    !viewModel.isQuizMode -> R.string.texto18
                    else -> R.string.texto17
                }

                Text(
                    text = stringResource(id = buttonTextResId),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Elementos arrastrables y zonas objetivo.
        // Solo visibles mientras el juego está en modo drag & drop.
        if (!viewModel.isQuizMode) {

            // Dibuja las zonas objetivo
            viewModel.items.forEach { item ->
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                item.targetPosition.x.roundToInt(),
                                item.targetPosition.y.roundToInt()
                            )
                        }
                        .size(90.dp)
                        .border(
                            3.dp,
                            Color.White.copy(alpha = 0.6f),
                            CircleShape
                        )
                        .background(
                            Color.Green.copy(alpha = 0.2f),
                            CircleShape
                        )
                )
            }

            // Dibuja los objetos móviles arrastrables
            viewModel.items.forEach { item ->
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Objeto",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                item.currentPosition.x.roundToInt(),
                                item.currentPosition.y.roundToInt()
                            )
                        }
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

        // Escenario final mostrado cuando el usuario completa correctamente toda la actividad
        // y desbloquea la recompensa.
        if (viewModel.isRewardUnlocked) {
            MensajeFinalActivity(
                titulo = stringResource(id = R.string.texto22),
                mensaje = stringResource(id = R.string.texto23),
                botonText = stringResource(id = R.string.texto24),
                onButtonClick = {
                    val ruta =
                        "endactivity/1/${R.drawable.act1_premio1}/${R.drawable.act1_premio2}"
                    navController.navigate(ruta) {
                        // Evita volver al juego al pulsar atrás desde la pantalla final
                        popUpTo("activity1") { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Componente reutilizable que representa una opción del quiz.
 *
 * @param textResId Recurso de texto que describe la opción.
 * @param id Identificador único de la opción.
 * @param selectedId Identificador de la opción actualmente seleccionada.
 * @param onSelect Callback ejecutado al seleccionar la opción.
 */
@Composable
fun QuizOption(
    @StringRes textResId: Int,
    id: Int,
    selectedId: Int,
    onSelect: () -> Unit
) {
    val isSelected = (id == selectedId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.onPrimary
            )
            .border(
                1.dp,
                if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onTertiaryContainer,
                RoundedCornerShape(8.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(id = textResId),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}