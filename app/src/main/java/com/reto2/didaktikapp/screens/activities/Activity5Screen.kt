package com.reto2.didaktikapp.screens.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.reto2.didaktikapp.R
import com.reto2.didaktikapp.components.MensajeFinalActivity
import com.reto2.didaktikapp.components.MensajeFinalActivity
import kotlin.math.absoluteValue

// ==========================================
// 1. CLASES DE DATOS Y LÓGICA DEL PUZZLE
// ==========================================

data class BoxInfo(
    val x: Float = 0f,
    val y: Float = 0f,
    val col: Int = -1,
    val row: Int = -1,
) {
    var isCorrecto: Boolean by mutableStateOf(false)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Activity5Screen(navController: NavController) {
    val image = ImageBitmap.imageResource(R.drawable.fondopuzzle)
    val shuffledPieces = remember { pieceMapv2.indices.shuffled() }

    // Bloqueo de rotación
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5))) {

        // Variables del juego
        val arrayBox = remember { Array(3) { Array(4) { BoxInfo() } } }
        var correctPieces by remember { mutableStateOf(0) }
        val isGameFinished = correctPieces == pieceMapv2.size

        // CAPA 1: JUEGO (FONDO)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tablero
            Column {
                for (row in arrayBox.indices) {
                    Row {
                        for (col in arrayBox[row].indices) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .alpha(0.5f)
                                    .drawBehind {
                                        drawImage(
                                            image = image,
                                            srcOffset = IntOffset(col * size.width.toInt(), row * size.height.toInt()),
                                            srcSize = IntSize(size.width.toInt(), size.height.toInt()),
                                            dstSize = IntSize(size.width.toInt(), size.height.toInt())
                                        )
                                    }
                                    .onGloballyPositioned { coords ->
                                        val box = arrayBox[row][col]
                                        arrayBox[row][col] = box.copy(
                                            x = coords.positionInWindow().x,
                                            y = coords.positionInWindow().y
                                        )
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Piezas desordenadas
            Box {
                shuffledPieces.forEach { piece ->
                    DraggablePieceMapAgujeros(
                        pieceIndex = piece,
                        image = image,
                        arrayBox = arrayBox,
                        onPlacedCorrectly = { correctPieces++ },
                        onRemovedCorrectly = { correctPieces-- }
                    )
                }
            }
        }

        // CAPA 2: MENSAJE FINAL
        if (isGameFinished) {
            MensajeFinalActivity(
                titulo = "¡PUZZLE COMPLETADO!",
                mensaje = "Has reconstruido el Cargadero de Minas correctamente.",
                botonText = "VER HISTORIA",
                onButtonClick = {
                    navController.navigate("EJ5Info")
                }
            )
        }
    }
}

// ==========================================
// 2. PANTALLA DE INFORMACIÓN (CON IA Y FALLBACK)
// ==========================================

@Composable
fun ventanaInfo(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(20.dp))

            // --- SECCIÓN 1 ---
            InfoSectionAI(
                titulo = stringResource(id = R.string.mensaje5_1),
                imagenId = R.drawable.copia_de_descarga_25,
                // Texto de respaldo (Fallback)
                textoPredeterminado = stringResource(id = R.string.texto65),
                // Pregunta a la IA
                promptIA = "Explica brevemente en español (máx 3 líneas) por qué llegaban trenes con hierro a los cargaderos de Barakaldo."
            )

            Spacer(Modifier.height(30.dp))

            Divider(
                color = MaterialTheme.colorScheme.scrim,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // --- SECCIÓN 2 ---
            InfoSectionAI(
                titulo = stringResource(id = R.string.mensaje5_2),
                imagenId = R.drawable.copia_de_descarga_26,
                textoPredeterminado = stringResource(id = R.string.texto66),
                promptIA = "Explica brevemente en español (máx 3 líneas) cómo se cargaba el hierro en los barcos en la Ría del Nervión para ir a Inglaterra."
            )

            Spacer(Modifier.height(30.dp))

            Divider(
                color = MaterialTheme.colorScheme.scrim,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // --- SECCIÓN 3 ---
            InfoSectionAI(
                titulo = stringResource(id = R.string.mensaje5_3),
                imagenId = R.drawable.copia_de_descarga_27,
                textoPredeterminado = stringResource(id = R.string.texto67),
                promptIA = "Explica brevemente en español (máx 3 líneas) la importancia económica de los cargaderos para el crecimiento de Barakaldo."
            )

            Spacer(Modifier.height(30.dp))

            // BOTÓN FINAL
            Button(
                onClick = {
                    val ruta = "endactivity/5/${R.drawable.premio71}/${R.drawable.premio22}"
                    navController.navigate(ruta) {
                        popUpTo("activity5") { inclusive = true }
                    }
                }
            ) {
                Text(stringResource(id = R.string.texto68))
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

// ==========================================
// 3. COMPONENTE INTELIGENTE (IA + FALLBACK)
// ==========================================

@Composable
fun InfoSectionAI(
    titulo: String,
    imagenId: Int,
    textoPredeterminado: String,
    promptIA: String
) {
    // Estado para controlar el texto que se muestra
    var textoAMostrar by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Título
    Text(
        text = titulo,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.scrim
    )

    Spacer(Modifier.height(20.dp))

    // Imagen
    Image(
        painter = painterResource(imagenId),
        contentDescription = null,
        modifier = Modifier.size(300.dp)
    )

    Spacer(Modifier.height(20.dp))

    // Lógica IA
    LaunchedEffect(Unit) {
        try {
            // Intentamos llamar a la IA
            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash")

            val response = model.generateContent(promptIA)

            // Mostramos el texto obtenido de la IA
            textoAMostrar = response.text.toString()
        } catch (e: Exception) {
            // ESTO ME DIRÁ EL ERROR EXACTO EN EL LOGCAT
            Log.e("ERROR_IA", "La IA falló por: ${e.message}")
            e.printStackTrace()

            textoAMostrar = textoPredeterminado
        } finally {
            isLoading = false
        }
    }

    // Renderizado del Texto
    if (isLoading) {
        // Mientras carga, mostramos un icono pre-loader
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = MaterialTheme.colorScheme.primary)
        }
    } else {
        // Mostramos el texto final (IA o Predeterminado)
        Text(
            text = textoAMostrar.ifEmpty { textoPredeterminado }, // Doble seguridad
            fontSize = 15.sp
        )
    }
}


// ==========================================
// 4. COMPONENTES DEL PUZZLE (SIN CAMBIOS)
// ==========================================

@Composable
fun DraggablePieceMapAgujeros(
    pieceIndex: Int,
    image: ImageBitmap,
    arrayBox: Array<Array<BoxInfo>>,
    onPlacedCorrectly: () -> Unit,
    onRemovedCorrectly: () -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }
    var isTouching by remember { mutableStateOf(false) }
    var wasCorrect by remember { mutableStateOf(false) }
    val shape = pieceMapv2[pieceIndex]

    PuzzlePieceShape(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(100.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += with(density) { dragAmount.x.toDp() }
                    offsetY += with(density) { dragAmount.y.toDp() }
                }
            }
            .onGloballyPositioned { coords ->
                val canvasRect = coords.boundsInWindow()
                val targetBox = arrayBox[shape.row][shape.column]
                val isCorrect =
                    (canvasRect.topLeft.y - targetBox.y).absoluteValue < 40 &&
                            (canvasRect.topLeft.x - targetBox.x).absoluteValue < 40
                if (isCorrect && !wasCorrect) {
                    wasCorrect = true
                    isTouching = true
                    onPlacedCorrectly()
                }
                if (!isCorrect && wasCorrect) {
                    wasCorrect = false
                    isTouching = false
                    onRemovedCorrectly()
                }
            },
        topHead = shape.topHead,
        topHole = shape.topHole,
        rightHead = shape.rightHead,
        rightHole = shape.rightHole,
        bottomHead = shape.bottomHead,
        bottomHole = shape.bottomHole,
        leftHead = shape.leftHead,
        leftHole = shape.leftHole,
        image = image,
        row = shape.row,
        column = shape.column,
        isTouching = isTouching
    )
}

@Composable
fun PuzzlePieceShape(
    modifier: Modifier = Modifier,
    topHead: Boolean = false,
    topHole: Boolean = false,
    rightHead: Boolean = false,
    rightHole: Boolean = false,
    bottomHead: Boolean = false,
    bottomHole: Boolean = false,
    leftHead: Boolean = false,
    leftHole: Boolean = false,
    image: ImageBitmap,
    row: Int,
    column: Int,
    isTouching: Boolean,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val knobSize = w * 0.2f
        val path = Path().apply {
            moveTo(0f, 0f)
            if (topHead) {
                lineTo(w * 0.4f, knobSize)
                cubicTo(w * 0.45f, 0f, w * 0.55f, 0f, w * 0.6f, knobSize)
                lineTo(w, 0f)
            } else if (topHole) {
                lineTo(w * 0.4f, 0f)
                cubicTo(w * 0.45f, +knobSize, w * 0.55f, +knobSize, w * 0.6f, 0f)
                lineTo(w, 0f)
            } else {
                lineTo(w, 0f)
            }
            if (rightHead) {
                lineTo(w, h * 0.4f)
                cubicTo(w + knobSize, h * 0.45f, w + knobSize, h * 0.55f, w, h * 0.6f)
                lineTo(w, h)
            } else if (rightHole) {
                lineTo(w+knobSize, h * 0.4f)
                cubicTo(w , h * 0.45f, w , h * 0.55f, w+knobSize, h * 0.6f)
                lineTo(w, h)
            } else {
                lineTo(w, h)
            }
            if (bottomHead) {
                lineTo(w * 0.6f, h)
                cubicTo(w * 0.55f, h + knobSize, w * 0.45f, h + knobSize, w * 0.4f, h)
                lineTo(0f, h)
            } else if (bottomHole) {
                lineTo(w * 0.6f, h+knobSize)
                cubicTo(w * 0.55f, h , w * 0.45f, h , w * 0.4f, h+knobSize)
                lineTo(0f, h)
            } else {
                lineTo(0f, h)
            }
            if (leftHead) {
                lineTo(knobSize, h * 0.6f)
                cubicTo(0f, h * 0.55f, 0f, h * 0.45f,knobSize , h * 0.4f)
                lineTo(0f, 0f)
            } else if (leftHole) {
                lineTo(0f, h * 0.6f)
                cubicTo(+knobSize, h * 0.55f, +knobSize, h * 0.45f, 0f, h * 0.4f)
                lineTo(0f, 0f)
            } else {
                lineTo(0f, 0f)
            }
            close()
        }
        clipPath(path) {
            drawImage(
                image = image,
                srcOffset = IntOffset(((column*w)).toInt(), ((row*h)).toInt()),
                srcSize = IntSize(((w+2*knobSize)).toInt(), ((h+2*knobSize)).toInt()),
                dstSize = IntSize((size.width+2*knobSize).toInt(), (size.height+2*knobSize).toInt())
            )
        }
        if(isTouching){
            drawPath(
                path = path,
                color = Color(0xFF39FF14),
                style = Stroke(width = 6f)
            )
        }
    }
}
data class PieceShapev4(
    val topHead: Boolean = false,
    val topHole: Boolean = false,
    val rightHead: Boolean = false,
    val rightHole: Boolean = false,
    val bottomHead: Boolean = false,
    val bottomHole: Boolean = false,
    val leftHead: Boolean = false,
    val leftHole: Boolean = false,
    val row: Int = -1,
    val column: Int = -1,
)
val pieceMapv2: List<PieceShapev4> = listOf(
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=0,column=0 ),
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=true,bottomHole=false,leftHead=false,leftHole=true,row=0,column=1 ),
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=1,column=0),
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=true,bottomHole=false,leftHead=true,leftHole=false,row=1,column=1),
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=2 ),
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=2),
    PieceShapev4(topHead = false, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=3),
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=3),
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=false,row=2,column=0),
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=1),
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=true,leftHole=false,row=2,column=2),
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=3)
)

@Preview
@Composable
fun PreviewVentanaInfo() {
    val mockNavController = rememberNavController()
    ventanaInfo(navController = mockNavController)
}