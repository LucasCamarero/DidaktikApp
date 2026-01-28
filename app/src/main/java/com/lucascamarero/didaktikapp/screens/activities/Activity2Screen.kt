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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity
import com.lucascamarero.didaktikapp.viewmodels.Game2ViewModel
import kotlin.math.roundToInt

/**
 * Pantalla principal de la Actividad 2.
 *
 * Muestra un juego de arrastrar y soltar donde el usuario debe asociar
 * correctamente una palabra con la imagen correspondiente.
 *
 * @param navController Controlador de navegación.
 * @param viewModel ViewModel que gestiona la lógica y el estado del juego.
 */
@Composable
fun Activity2Screen(
    navController: NavController,
    viewModel: Game2ViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Título de la actividad
            Text(
                text = stringResource(id = R.string.texto25) +
                        "(${viewModel.currentPhaseIndex + 1}/4)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta que contiene la imagen de la fase actual y la zona de drop para
            // la palabra seleccionada.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
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

                    // Zona inferior donde se muestra la palabra soltada o el mensaje de instrucción.
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                if (viewModel.isCorrectAnswer)
                                    MaterialTheme.colorScheme.scrim
                                else
                                    MaterialTheme.colorScheme.background
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.droppedWordRes != null) {
                            Text(
                                text = stringResource(viewModel.droppedWordRes!!),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Text(
                                stringResource(id = R.string.texto42),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // Sección inferior con mensaje de feedback y controles.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mensaje de feedback que informa si la respuesta es correcta o incorrecta.
                Text(
                    text = stringResource(viewModel.feedbackMessageRes),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = when {
                        viewModel.isCorrectAnswer -> MaterialTheme.colorScheme.scrim
                        viewModel.feedbackMessageRes == R.string.texto40 -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.tertiary
                    },
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Botón para avanzar a la siguiente fase si la respuesta es correcta,
                // o listado de opciones arrastrables en caso contrario.
                if (viewModel.isCorrectAnswer) {
                    Button(
                        onClick = { viewModel.nextPhase() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.texto43),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val rows = viewModel.currentPhase.optionsRes.chunked(2)
                        rows.forEach { rowWords ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowWords.forEach { wordRes ->
                                    DraggableOption(
                                        textRes = wordRes,
                                        onDrop = { droppedRes ->
                                            viewModel.checkAnswer(droppedRes)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mensaje final que se muestra al completar todas las fases del juego.
        if (viewModel.isGameFinished) {
            MensajeFinalActivity(
                titulo = stringResource(id = R.string.texto44),
                mensaje = stringResource(id = R.string.texto45),
                botonText = stringResource(id = R.string.texto46),
                onButtonClick = {
                    val ruta =
                        "endactivity/2/${R.drawable.premio11}/${R.drawable.premio62}"
                    navController.navigate(ruta) {
                        popUpTo("activity2") { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Composable que representa una opción arrastrable.
 *
 * Permite al usuario arrastrar una palabra y soltarla sobre la imagen
 * para comprobar si es la opción correcta.
 *
 * @param textRes Recurso string de la palabra a mostrar.
 * @param onDrop Callback que se ejecuta cuando la palabra es soltada.
 */
@Composable
fun DraggableOption(
    textRes: Int,
    onDrop: (Int) -> Unit
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
                        if (offsetY < -100) {
                            onDrop(textRes)
                        }
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
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurfaceVariant,
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(textRes),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}