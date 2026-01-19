package com.lucascamarero.didaktikapp.screens.activities

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
// 1. IMPORTAMOS TU COMPONENTE
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity
import com.lucascamarero.didaktikapp.viewmodels.Game4ViewModel
import kotlinx.coroutines.delay

// --- 1. MODELO DE DATOS ---
data class ToolItem(val id: Int, val name: String, val iconRes: Int)

// --- 2. UTILIDAD PARA EL EFECTO NEÓN ---
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

        // Dibujar Fondo
        frameworkPaint.color = containerColor.toArgb()
        frameworkPaint.style = android.graphics.Paint.Style.FILL
        frameworkPaint.maskFilter = null
        canvas.drawRoundRect(0f, 0f, size.width, size.height, cornerRadius.toPx(), cornerRadius.toPx(), paint)

        // Dibujar Neón
        frameworkPaint.color = glowColor.toArgb()
        frameworkPaint.style = android.graphics.Paint.Style.STROKE
        frameworkPaint.strokeWidth = glowThickness.toPx()
        frameworkPaint.maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
        canvas.drawRoundRect(0f, 0f, size.width, size.height, cornerRadius.toPx(), cornerRadius.toPx(), paint)
    }
}

// --- 3. PANTALLA PRINCIPAL ---
@Composable
fun Activity4Screen(
    navController: NavController,
    viewModel: Game4ViewModel = hiltViewModel()
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

    // Recolectamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Estado local para controlar cuándo mostrar el modal (después del delay visual)
    var showSuccessModal by remember { mutableStateOf(false) }

    // ===================================================================
    // 2. LÓGICA DE TIEMPO (DELAY VISUAL)
    // ===================================================================
    if (uiState.isGameWon) {
        LaunchedEffect(Unit) {
            // Esperamos 1.5 segundos viendo el edificio iluminado
            delay(1500)
            // Activamos el modal
            showSuccessModal = true
        }
    }

    // Usamos Box para superponer capas
    Box(modifier = Modifier.fillMaxSize()) {

        // ===================================================================
        // CAPA 1: JUEGO (FONDO)
        // ===================================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- SECCIÓN 1: EDIFICIO (CAMBIA SI GANAS) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7D9EAA))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val imageRes = if (uiState.isGameWon) {
                        R.drawable.activ4_edificio_ilgner_iluminado
                    } else {
                        R.drawable.activ4_edificio_ilgner_oscuro
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Edificio Ilgner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // --- MENSAJE DE TEXTO ---
            Text(
                text = uiState.message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (uiState.message.contains("incorrecto", true) || uiState.message.contains("faltan", true)) Color.Red else Color.Black
            )

            // --- SECCIÓN 2: GRID DE OBJETOS ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
            ) {
                val columns = 3
                val chunkedTools = uiState.tools.chunked(columns)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chunkedTools.forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { tool ->
                                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                    ToolGridItem(
                                        tool = tool,
                                        isSelected = uiState.selectedIds.contains(tool.id),
                                        onItemClick = {
                                            if (!uiState.isGameWon) {
                                                viewModel.toggleSelection(tool.id)
                                            }
                                        }
                                    )
                                }
                            }
                            val missing = columns - rowItems.size
                            repeat(missing) { Spacer(modifier = Modifier.weight(1f)) }
                        }
                    }
                }
            }

            // --- SECCIÓN 3: BOTÓN ENCENDER ---
            Button(
                onClick = { viewModel.checkAnswer() },
                enabled = !uiState.isGameWon,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF154c79),
                    disabledContainerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (uiState.isGameWon) "¡CONECTADO!" else "ENCENDER",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        // ===================================================================
        // CAPA 2: MENSAJE FINAL (POP-UP)
        // ===================================================================
        if (showSuccessModal) {
            MensajeFinalActivity(
                titulo = "¡ENERGÍA RESTAURADA!",
                mensaje = "Has conectado correctamente el sistema eléctrico del Edificio Ilgner.",
                botonText = "VER RESULTADO",
                onButtonClick = {
                    // ID = 4
                    val ruta = "endactivity/4/${R.drawable.activ4_edificio_ilgner_oscuro}/${R.drawable.activ4_edificio_ilgner_iluminado}"
                    navController.navigate(ruta) {
                        popUpTo("activity4") { inclusive = true }
                    }
                }
            )
        }
    }
}

// --- 4. COMPONENTE DE ITEM INDIVIDUAL (Sin cambios) ---
@Composable
fun ToolGridItem(
    tool: ToolItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    val neonColor = Color(0xFFFFFF00)
    val selectedBackgroundColor = Color(0xFF444444)

    val modifierWithSelection = if (isSelected) {
        Modifier
            .neonGlow(
                glowColor = neonColor,
                containerColor = selectedBackgroundColor,
                blurRadius = 25.dp,
                glowThickness = 12.dp,
                cornerRadius = 12.dp
            )
            .border(2.dp, neonColor, RoundedCornerShape(12.dp))
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .then(modifierWithSelection),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Transparent else Color.LightGray
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