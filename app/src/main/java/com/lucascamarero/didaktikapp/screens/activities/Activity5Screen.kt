package com.lucascamarero.didaktikapp.screens.activities


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
@Composable
fun Activity5Screen(navController: NavController){
    var showInfo by remember { mutableStateOf(true) }
    var showCompletion by remember { mutableStateOf(false) }
    val pieces = remember {
        listOf(
            PuzzlePiece(id = 0, color = Color(0xFFE74C3C), shape = PieceShape(topHead = false, rightHole = true, bottomHead = true, leftHole = false)),
            PuzzlePiece(id = 1, color = Color(0xFF2ECC71), shape = PieceShape(topHead = false, rightHole = false, bottomHead = true, leftHole = true)),
            PuzzlePiece(id = 2, color = Color(0xFF3498DB), shape = PieceShape(topHead = true, rightHole = true, bottomHead = false, leftHole = false)),
            PuzzlePiece(id = 3, color = Color(0xFFF1C40F), shape = PieceShape(topHead = true, rightHole = false, bottomHead = false, leftHole = true))
        )
    }

    val positions = listOf(
        Offset(40f, 200f), Offset(200f, 200f),
        Offset(40f, 360f), Offset(200f, 360f)
    )

    var placedPieces by remember { mutableStateOf(0) }

    if (showInfo) {
        Dialog(onDismissRequest = { showInfo = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Rompecabezas del Cargadero",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "En los cargaderos se cargaba el hierro que venía de las minas para llevarlo en barco.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showInfo = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Text("Comenzar", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    if (showCompletion) {
        Dialog(onDismissRequest = { showCompletion = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Puzzle Completado!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Datos curiosos
                    listOf(
                        "Por aquí llegaban los trenes con el hierro.",
                        "El hierro se cargaba en los barcos que iban a otros países.",
                        "Los cargaderos ayudaron a que Barakaldo creciera mucho."
                    ).forEach { fact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF2E7D32), RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = fact,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            showCompletion = false
                            placedPieces = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Text("Jugar otra vez", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tablero del puzzle (2x2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            // Marcas de las posiciones
            positions.forEachIndexed { index, position ->
                Box(
                    modifier = Modifier
                        .offset(x = position.x.dp, y = position.y.dp)
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contador
        Text(
            text = "Piezas colocadas: $placedPieces/4",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Piezas para arrastrar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            pieces.forEach { piece ->
                DraggablePiece(
                    piece = piece,
                    targetPositions = positions,
                    onPiecePlaced = {
                        placedPieces++
                        if (placedPieces == 4) {
                            showCompletion = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DraggablePiece(
    piece: PuzzlePiece,
    targetPositions: List<Offset>,
    onPiecePlaced: () -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf((piece.id * 100 + 20).toFloat()) }
    var offsetY by remember { mutableStateOf(20f) }
    var isPlaced by remember { mutableStateOf(false) }

    if (isPlaced) return

    Box(
        modifier = Modifier
            .offset(x = offsetX.dp, y = offsetY.dp)
            .size(80.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += with(density) { dragAmount.x.toDp().value }
                    offsetY += with(density) { dragAmount.y.toDp().value }

                    // Verificar si está cerca de su posición correcta
                    val targetPos = targetPositions[piece.id]
                    if (kotlin.math.abs(offsetX - targetPos.x) < 40 &&
                        kotlin.math.abs(offsetY - targetPos.y) < 40) {
                        offsetX = targetPos.x
                        offsetY = targetPos.y
                        isPlaced = true
                        onPiecePlaced()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val knobSize = w * 0.2f

            val path = Path().apply {
                moveTo(0f, 0f)

                // Superior
                if (piece.shape.topHead) {
                    lineTo(w * 0.4f, 0f)
                    cubicTo(w * 0.45f, -knobSize, w * 0.55f, -knobSize, w * 0.6f, 0f)
                    lineTo(w, 0f)
                } else if (piece.shape.topHole) {
                    lineTo(w * 0.4f, 0f)
                    cubicTo(w * 0.45f, knobSize, w * 0.55f, knobSize, w * 0.6f, 0f)
                    lineTo(w, 0f)
                } else {
                    lineTo(w, 0f)
                }

                // Derecha
                if (piece.shape.rightHead) {
                    lineTo(w, h * 0.4f)
                    cubicTo(w + knobSize, h * 0.45f, w + knobSize, h * 0.55f, w, h * 0.6f)
                    lineTo(w, h)
                } else if (piece.shape.rightHole) {
                    lineTo(w, h * 0.4f)
                    cubicTo(w - knobSize, h * 0.45f, w - knobSize, h * 0.55f, w, h * 0.6f)
                    lineTo(w, h)
                } else {
                    lineTo(w, h)
                }

                // Inferior
                if (piece.shape.bottomHead) {
                    lineTo(w * 0.6f, h)
                    cubicTo(w * 0.55f, h + knobSize, w * 0.45f, h + knobSize, w * 0.4f, h)
                    lineTo(0f, h)
                } else if (piece.shape.bottomHole) {
                    lineTo(w * 0.6f, h)
                    cubicTo(w * 0.55f, h - knobSize, w * 0.45f, h - knobSize, w * 0.4f, h)
                    lineTo(0f, h)
                } else {
                    lineTo(0f, h)
                }

                // Izquierda
                if (piece.shape.leftHead) {
                    lineTo(0f, h * 0.6f)
                    cubicTo(-knobSize, h * 0.55f, -knobSize, h * 0.45f, 0f, h * 0.4f)
                    lineTo(0f, 0f)
                } else if (piece.shape.leftHole) {
                    lineTo(0f, h * 0.6f)
                    cubicTo(knobSize, h * 0.55f, knobSize, h * 0.45f, 0f, h * 0.4f)
                    lineTo(0f, 0f)
                } else {
                    lineTo(0f, 0f)
                }

                close()
            }

            drawPath(path, piece.color, style = Fill)
            drawPath(path, Color.White, style = Stroke(3f))

            // Número de la pieza
            drawContext.canvas.nativeCanvas.drawText(
                "${piece.id + 1}",
                w * 0.5f,
                h * 0.5f + 10,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

data class PuzzlePiece(
    val id: Int,
    val color: Color,
    val shape: PieceShape
)

data class PieceShape(
    val topHead: Boolean = false,
    val topHole: Boolean = false,
    val rightHead: Boolean = false,
    val rightHole: Boolean = false,
    val bottomHead: Boolean = false,
    val bottomHole: Boolean = false,
    val leftHead: Boolean = false,
    val leftHole: Boolean = false
)