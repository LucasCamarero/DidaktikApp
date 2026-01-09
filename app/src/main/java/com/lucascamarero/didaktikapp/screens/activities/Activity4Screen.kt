package com.lucascamarero.didaktikapp.screens.activities

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R

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
    navController: NavController? = null // Hacemos nulo el controller para facilitar la preview
) {
    // DEFINICIÓN DE LOS DATOS
    val toolsList = remember {
        listOf(
            ToolItem(1, "Bombilla", R.drawable.activ4_bombilla),    // CORRECTO
            ToolItem(2, "Casco", R.drawable.activ4_casco),
            ToolItem(3, "Cables", R.drawable.activ4_cable),         // CORRECTO
            ToolItem(4, "Martillo", R.drawable.activ4_martillo),
            ToolItem(5, "Generador", R.drawable.activ4_generador),  // CORRECTO
            ToolItem(6, "Llave", R.drawable.activ4_llave)           // CORRECTO
        )
    }

    // DEFINICIÓN DE LA SOLUCIÓN
    val correctIds = remember { setOf(1, 3, 5, 6) }

    // ESTADOS (VARIABLES QUE CAMBIAN)
    var selectedIds by remember { mutableStateOf(setOf<Int>()) }
    var isGameWon by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("Elige los objetos que puedan encender el edificio Ilgner.") }

    // --- INTERFAZ DE USUARIO ---
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                // LÓGICA DE LA IMAGEN
                val imageRes = if (isGameWon) {
                    R.drawable.activ4_edificio_ilgner_iluminado // Imagen encendida
                } else {
                    R.drawable.activ4_edificio_ilgner_oscuro    // Imagen apagada
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
            text = feedbackMessage,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = if (feedbackMessage.contains("incorrecto", true)) Color.Red else Color.Black
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
            val chunkedTools = toolsList.chunked(columns)

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
                                    isSelected = selectedIds.contains(tool.id),
                                    onItemClick = {
                                        // SI YA GANÓ, NO DEJAMOS TOCAR NADA MÁS
                                        if (!isGameWon) {
                                            // LÓGICA DE SELECCIÓN
                                            val current = selectedIds.toMutableSet()
                                            if (current.contains(tool.id)) {
                                                current.remove(tool.id)
                                            } else {
                                                current.add(tool.id)
                                            }
                                            selectedIds = current
                                            // Reiniciamos mensaje al tocar
                                            feedbackMessage = "Elige los objetos que puedan encender el edificio Ilgner."
                                        }
                                    }
                                )
                            }
                        }
                        // Rellenar huecos
                        val missing = columns - rowItems.size
                        repeat(missing) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }
        }

        // --- SECCIÓN 3: BOTÓN ENCENDER ---
        Button(
            onClick = {
                // LÓGICA DE VALIDACIÓN
                if (selectedIds == correctIds) {
                    isGameWon = true
                    feedbackMessage = "¡Bien hecho! El sistema eléctrico funciona correctamente."
                } else {
                    isGameWon = false
                    feedbackMessage = "Objeto incorrecto o faltan herramientas. Inténtalo de nuevo."
                }
            },
            enabled = !isGameWon, // Se deshabilita al ganar
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF154c79),
                disabledContainerColor = Color(0xFF4CAF50) // Se pone verde al ganar
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isGameWon) "¡CONECTADO!" else "ENCENDER",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

// --- 4. COMPONENTE DE ITEM INDIVIDUAL ---
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
            .then(modifierWithSelection), // IMPORTANTE: EL CLICK NO VA AQUÍ
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Transparent else Color.LightGray
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onItemClick() } // <--- ¡AQUÍ ES DONDE FUNCIONA EL CLICK!
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

@Preview(showBackground = true)
@Composable
fun Activity4ScreenPreview() {
    Activity4Screen()
}