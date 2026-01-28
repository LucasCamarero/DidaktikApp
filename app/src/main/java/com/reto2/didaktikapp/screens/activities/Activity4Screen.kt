package com.reto2.didaktikapp.screens.activities

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reto2.didaktikapp.components.MensajeFinalActivity
import com.reto2.didaktikapp.viewmodels.Game4ViewModel
import kotlinx.coroutines.delay
import com.reto2.didaktikapp.R

/**
 * Modelo de datos que representa una herramienta interactiva del juego.
 *
 * @property id Identificador único de la herramienta
 * @property name Nombre descriptivo de la herramienta
 * @property iconRes Recurso drawable del icono asociado
 */
data class ToolItem(val id: Int, val name: String, val iconRes: Int)

/**
 * Extensión de Modifier que aplica un efecto visual de resplandor (neón)
 * alrededor de un contenedor con esquinas redondeadas.
 *
 * @param glowColor Color del resplandor
 * @param containerColor Color de fondo del contenedor
 * @param blurRadius Intensidad del desenfoque del resplandor
 * @param glowThickness Grosor del borde luminoso
 * @param cornerRadius Radio de las esquinas redondeadas
 */
fun Modifier.neonGlow(
    glowColor: Color,
    containerColor: Color,
    blurRadius: Dp = 20.dp,
    glowThickness: Dp = 20.dp,
    cornerRadius: Dp = 12.dp
) = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        frameworkPaint.color = containerColor.toArgb()
        frameworkPaint.style = android.graphics.Paint.Style.FILL
        frameworkPaint.maskFilter = null
        canvas.drawRoundRect(
            0f, 0f, size.width, size.height,
            cornerRadius.toPx(), cornerRadius.toPx(), paint
        )

        frameworkPaint.color = glowColor.toArgb()
        frameworkPaint.style = android.graphics.Paint.Style.STROKE
        frameworkPaint.strokeWidth = glowThickness.toPx()
        frameworkPaint.maskFilter =
            BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
        canvas.drawRoundRect(
            0f, 0f, size.width, size.height,
            cornerRadius.toPx(), cornerRadius.toPx(), paint
        )
    }
}

/**
 * Pantalla principal de la Actividad 4.
 *
 * Muestra:
 * - El estado visual del edificio
 * - El mensaje informativo del juego
 * - El grid de herramientas seleccionables
 * - El botón de comprobación
 * - El mensaje final al completar correctamente la actividad
 *
 * @param navController Controlador de navegación
 * @param viewModel ViewModel que gestiona el estado del juego
 */
@Composable
fun Activity4Screen(
    navController: NavController,
    viewModel: Game4ViewModel = hiltViewModel()
) {
    /**
     * Estado observable de la UI proporcionado por el ViewModel.
     */
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Controla la visualización del modal final tras un retardo visual.
     */
    var showSuccessModal by remember { mutableStateOf(false) }

    /**
     * Retardo visual que permite mostrar el edificio iluminado
     * antes de abrir el mensaje final.
     */
    if (uiState.isGameWon) {
        LaunchedEffect(Unit) {
            delay(3500)
            showSuccessModal = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Contenedor principal del juego.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Sección visual del edificio. Cambia su imagen dependiendo del estado del juego.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                val imageRes =
                    if (uiState.isGameWon)
                        R.drawable.activ4_edificio_ilgner_iluminado
                    else
                        R.drawable.activ4_edificio_ilgner_oscuro

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Edificio Ilgner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Mensaje informativo del estado del juego. C
            // ambia de color si el mensaje representa un error.
            val messageText = stringResource(uiState.messageRes)
            val errorWords = listOf(
                stringResource(R.string.texto55),
                stringResource(R.string.texto56)
            )

            Text(
                text = messageText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (errorWords.any { messageText.contains(it, true) })
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.tertiary
            )

            // Grid de herramientas seleccionables.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                val columns = 3
                val rows = uiState.tools.chunked(columns)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { tool ->
                                ToolGridItem(
                                    tool = tool,
                                    isSelected = uiState.selectedIds.contains(tool.id),
                                    onItemClick = {
                                        if (!uiState.isGameWon) {
                                            viewModel.toggleSelection(tool.id)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                            repeat(columns - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.checkAnswer() },
                enabled = !uiState.isGameWon,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.scrim
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(
                        if (uiState.isGameWon)
                            R.string.texto57
                        else
                            R.string.texto58
                    ),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        // Modal final que se muestra al completar correctamente la actividad.
        if (showSuccessModal) {
            MensajeFinalActivity(
                titulo = stringResource(R.string.texto59),
                mensaje = stringResource(R.string.texto60),
                botonText = stringResource(R.string.texto61),
                onButtonClick = {
                    val ruta =
                        "endactivity/4/${R.drawable.premio21}/${R.drawable.premio72}"
                    navController.navigate(ruta) {
                        popUpTo("activity4") { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Componente que representa una herramienta individual dentro del grid.
 *
 * Aplica un efecto visual destacado cuando la herramienta está seleccionada.
 *
 * @param tool Herramienta representada
 * @param isSelected Indica si la herramienta está seleccionada
 * @param onItemClick Acción al pulsar la herramienta
 * @param modifier Modifier externo para control de layout
 */
@Composable
fun ToolGridItem(
    tool: ToolItem,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neonColor = MaterialTheme.colorScheme.secondaryContainer
    val selectedBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer

    val modifierWithSelection =
        if (isSelected) {
            modifier
                .neonGlow(
                    glowColor = neonColor,
                    containerColor = selectedBackgroundColor,
                    blurRadius = 25.dp,
                    glowThickness = 12.dp,
                    cornerRadius = 12.dp
                )
                .border(2.dp, neonColor, RoundedCornerShape(12.dp))
        } else modifier

    Card(
        modifier = modifierWithSelection.aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isSelected)
                    Color.Transparent
                else
                    MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onItemClick() }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = tool.iconRes),
                contentDescription = tool.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}