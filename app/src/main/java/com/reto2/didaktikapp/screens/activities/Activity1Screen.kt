package com.reto2.didaktikapp.screens.activities

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reto2.didaktikapp.components.MensajeFinalActivity
import com.reto2.didaktikapp.viewmodels.GameViewModel
import kotlin.math.roundToInt
import com.reto2.didaktikapp.R

/**
 * Pantalla principal de la Actividad 1.
 *
 * Gestiona todo el flujo del juego:
 * - Escenario visual principal
 * - Ejercicio de arrastre (drag & drop)
 * - Zona de paneles contenedores
 * - Modo quiz
 * - Botón de acción principal
 * - Mensaje final de recompensa
 *
 * La lógica de negocio y el estado del juego se delegan completamente
 * al [GameViewModel], manteniendo esta pantalla enfocada en la UI.
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param viewModel ViewModel que gestiona el estado y la lógica del juego.
 */
@Composable
fun Activity1Screen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {

    /**
     * Contenedor raíz de la pantalla.
     *
     * Permite superponer:
     * - Escenario principal
     * - Objetos arrastrables
     * - Zonas objetivo
     * - Mensaje final
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen principal del escenario del juego.
            Image(
                painter = painterResource(id = R.drawable.act1_bg_game2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(2f)
                    .clip(RoundedCornerShape(12.dp))
            )

            // Texto informativo que muestra el estado actual del juego.
            // El contenido y el color dependen del estado gestionado por el ViewModel.
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

            // ZONA DE PANELES AZULES CON OBJETOS ARRASTRABLES
            if (!viewModel.isQuizMode) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.items.forEach { item ->
                        var localOffset by remember { mutableStateOf(Offset.Zero) }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .zIndex(1f)
                                    .offset {
                                        IntOffset(
                                            localOffset.x.roundToInt(),
                                            localOffset.y.roundToInt()
                                        )
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()

                                            // Movimiento visual inmediato
                                            localOffset += dragAmount

                                            // Delegación de la lógica al ViewModel
                                            viewModel.updateItemPosition(item.id, dragAmount)
                                        }
                                    }
                            )
                        }
                    }
                }
            } else {
                // MODO QUIZ
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizOption(R.string.texto19, 1, viewModel.selectedQuizOption) {
                        viewModel.selectedQuizOption = 1
                    }
                    QuizOption(R.string.texto20, 2, viewModel.selectedQuizOption) {
                        viewModel.selectedQuizOption = 2
                    }
                    QuizOption(R.string.texto21, 3, viewModel.selectedQuizOption) {
                        viewModel.selectedQuizOption = 3
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onMainButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(
                        when {
                            !viewModel.isDragSuccess -> R.string.texto17
                            !viewModel.isQuizMode -> R.string.texto18
                            else -> R.string.texto17
                        }
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ZONAS OBJETIVO (DROP TARGETS)
        if (!viewModel.isQuizMode) {
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
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color.Green.copy(alpha = 0.25f), CircleShape)
                )
            }
        }
        // MENSAJE FINAL DE RECOMPENSA
        if (viewModel.isRewardUnlocked) {
            MensajeFinalActivity(
                titulo = stringResource(id = R.string.texto22),
                mensaje = stringResource(id = R.string.texto23),
                botonText = stringResource(id = R.string.texto24),
                onButtonClick = {
                    navController.navigate(
                        "endactivity/1/${R.drawable.premio41}/${R.drawable.premio32}"
                    ) {
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
    val isSelected = id == selectedId

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
                MaterialTheme.colorScheme.onPrimaryContainer,
                RoundedCornerShape(8.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(id = textResId),
            fontSize = 16.sp
        )
    }
}