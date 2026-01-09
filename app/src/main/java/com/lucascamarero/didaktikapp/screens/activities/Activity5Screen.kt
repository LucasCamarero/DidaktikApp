package com.lucascamarero.didaktikapp.screens.activities

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.lucascamarero.didaktikapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


data class BoxInfo(
    //Guarda información de cada espacio del tablero
    val x: Float = 0f, //Posición X en pantalla
    val y: Float = 0f, //Posición Y en pantalla
    val col: Int = -1, //Columna
    val row: Int = -1, // Fila
){
    var isCorrecto: Boolean by mutableStateOf(false)
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Activity5Screen(navController: NavController) {


    val image = ImageBitmap.imageResource(R.drawable.fondopuzzle)
    //total de piezas reales
    val totalPieces = pieceMapv2.size
    //Guarda el orden una sola vez
    val shuffledPieces = remember { pieceMapv2.indices.shuffled() }
    //evita navegar varias veces
    var finished by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        //matriz del tablero (3x4)
        val arrayBox = remember { Array(3) { Array(4) { BoxInfo() } } }


        //contar cuántas piezas están bien colocadas
        var correctPieces by remember { mutableStateOf(0) }


        //navegación automática al completar puzzle
        if (correctPieces == pieceMapv2.size && !finished) {
            finished = true
            scope.launch {
                delay(5000)
                navController.navigate("EJ5Info")
            }
        }


        // ===== TABLERO SUPERIOR =====
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
                                        srcOffset = IntOffset(
                                            col * size.width.toInt(),
                                            row * size.height.toInt()
                                        ),
                                        srcSize = IntSize(
                                            size.width.toInt(),
                                            size.height.toInt()
                                        ),
                                        dstSize = IntSize(
                                            size.width.toInt(),
                                            size.height.toInt()
                                        )
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


        // ===== PIEZAS ABAJO =====
        Box {
            shuffledPieces.forEach  { piece ->
                DraggablePieceMapAgujeros(
                    pieceIndex = piece,
                    image = image,
                    arrayBox = arrayBox,
                    onPlacedCorrectly = {
                        correctPieces++
                    },
                    onRemovedCorrectly = {
                        correctPieces--
                    }
                )
            }
        }
    }
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
    Canvas(modifier = modifier) { //Area de dibujo personalizada
        val w = size.width
        val h = size.height //Tamaño de la pieza
        val knobSize = w * 0.2f //Tamaño de las orejas del puzzle


        val path = Path().apply { //Define la forma exacta de la pieza
            moveTo(0f, 0f)


            // borde superior
            //Decide si hay:
            if (topHead) { // Cabeza
                //lineTo(0f, knobSize)
                lineTo(w * 0.4f, knobSize)
                cubicTo(w * 0.45f, 0f, w * 0.55f, 0f, w * 0.6f, knobSize)
                lineTo(w, 0f)


            } else if (topHole) { //Hueco
                lineTo(w * 0.4f, 0f)
                cubicTo(w * 0.45f, +knobSize, w * 0.55f, +knobSize, w * 0.6f, 0f)
                lineTo(w, 0f)
            } else { //Linea recta
                lineTo(w, 0f)
            }


            // borde derecho
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


            // borde inferior
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


            // borde izquierdo
            if (leftHead) {


                lineTo(knobSize, h * 0.6f)
                cubicTo(0f, h * 0.55f, 0f, h * 0.45f,knobSize , h * 0.4f)
                //cubicTo(w , h * 0.45f, w , h * 0.55f, w+knobSize, h * 0.6f)


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




        clipPath(path) { //Recorta la imagen con la forma del rompecabezas
            drawImage(
                image = image,
                srcOffset = IntOffset(((column*w)).toInt(), ((row*h)).toInt()),
                srcSize = IntSize(((w+2*knobSize)).toInt(), ((h+2*knobSize)).toInt()),
                dstSize = IntSize((size.width+2*knobSize).toInt(), (size.height+2*knobSize).toInt())
            )
        }
        if(isTouching){ //Si está bien posicionada se dibuja borde verde
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
/*
* Describe cómo es cada pieza:
Qué lados tienen cabeza o hueco
A qué fila y columna pertenece
*/
val pieceMapv2: List<PieceShapev4> = listOf( //Define las 4 piezas del puzzle
    // Fila 0, Columna 0
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=0,column=0 ),
    // Fila 0, Columna 1
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=true,bottomHole=false,leftHead=false,leftHole=true,row=0,column=1 ),
    // Fila 1, Columna 0
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=false,leftHole=false,row=1,column=0),
    // Fila 1, Columna 1
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=true,bottomHole=false,leftHead=true,leftHole=false,row=1,column=1),
    //Fila 0, Columna 2
    PieceShapev4(topHead = false, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=2 ),
    //FIla 1, Columna 2
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = true,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=2),
    //Fila 0, Columna 3
    PieceShapev4(topHead = false, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=false,leftHole=true,row=0,column=3),
    //Fila 1, Columna 3
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=true,leftHead=true,leftHole=false,row=1,column=3),
    //Fila 2, Columna 0
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=false,row=2,column=0),
    //Fila 2, Columna 1
    PieceShapev4(topHead = false, topHole = true, rightHead = false, rightHole = true,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=1),
    //Fila 2, Columna 2
    PieceShapev4(topHead = true, topHole = false, rightHead = true, rightHole = false,bottomHead=false,bottomHole=false,leftHead=true,leftHole=false,row=2,column=2),
    //Fila 2, Columna 3
    PieceShapev4(topHead = true, topHole = false, rightHead = false, rightHole = false,bottomHead=false,bottomHole=false,leftHead=false,leftHole=true,row=2,column=3)
)
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
    var wasCorrect by remember { mutableStateOf(false) } // 🔥 CLAVE


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
                    (canvasRect.topLeft.y - targetBox.y).absoluteValue < 10 &&
                            (canvasRect.topLeft.x - targetBox.x).absoluteValue < 10


                if (isCorrect && !wasCorrect) {
                    wasCorrect = true
                    isTouching = true
                    onPlacedCorrectly() //  SUMA
                }


                if (!isCorrect && wasCorrect) {
                    wasCorrect = false
                    isTouching = false
                    onRemovedCorrectly() // RESTA
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
fun ventanaInfo(navController: NavController) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            InfoSection( // Llamada al componente reutilizable InfoSection con sus parámetros
                titulo = "Por aquí llegaban los trenes con el hierro", // Título de la primera sección
                imagenId = R.drawable.copia_de_descarga_25, // ID del recurso de imagen para la primera sección
                // Pregunta para el modelo de IA
                pregunta = "Contestame a la pregunta en castellano y corto, que no sea mas de 4 lineas: ¿Por cargadero de Baracaldo llegaban los trenes con el hierro?",
                // Texto alternativo que se muestra si hay error en la consulta a IA
                textoAlt = "Por qué: En esta foto se ve muy bien la estructura alargada que conecta la tierra con el cargadero. Puedes explicar que los trenes circulaban por esa parte superior para descargar el mineral directamente desde los vagones"
            )
            Divider(
                color = Color.Black, // Color negro para la línea
                thickness = 8.dp, // Grosor de 8 dp (density-independent pixels)
                modifier = Modifier.padding(horizontal = 16.dp) // Margen horizontal de 16 dp a cada lado
            )
            InfoSection( // Segunda sección de información
                titulo = "El hierro se cargaba en los barcos que iban a otros países",
                imagenId = R.drawable.copia_de_descarga_26,
                pregunta = "Contestame a la pregunta en castellano y corto, que no sea mas de 4 lineas: ¿El hierro se cargaba en los barcos que iban a otros países?",
                textoAlt = "Por qué: Al ser una toma desde arriba, se ve claramente la posición del cargadero dentro del río (la Ría del Nervión). Es la imagen perfecta para que el alumnado imagine un gran barco atracado junto a la estructura de madera esperando a ser llenado de hierro para viajar a Inglaterra o Francia"
            )
            Divider(
                color = Color.Black,
                thickness = 8.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            InfoSection( // Tercera sección de información
                titulo = "Los cargaderos ayudaron a que Barakaldo creciera mucho",
                imagenId = R.drawable.copia_de_descarga_27,
                pregunta = "Contestame a la pregunta en castellano y corto, que no sea mas de 4 lineas: ¿Los cargaderos ayudaron a que Barakaldo creciera mucho?",
                textoAlt = "Por qué: Esta imagen muestra la fuerza y la magnitud de la construcción de madera y hierro. Representa el \"corazón\" de la industria que trajo trabajo, personas y riqueza, convirtiendo a Barakaldo en la gran ciudad que es hoy"
            )
            Button( // Botón para navegar a la pantalla final
                onClick = {
                    navController.navigate(
                        "finActividad/${R.drawable.cargaderos_antigua}/${R.drawable.ferrocarril_actual}"
                    )
                }
            ) {
                Text("Ventana final")
            }
        }
    }
}
@Composable
fun InfoSection(
    titulo: String,        // Título de la sección
    imagenId: Int,         // ID del recurso de imagen
    pregunta: String,      // Pregunta para la IA
    textoAlt: String  // Texto alternativo en caso de error
) {
    var isLoading by remember { mutableStateOf(false) } // Estado que indica si está cargando la respuesta de la IA
    var texto by remember { mutableStateOf("") } // Estado que almacena el texto generado por la IA

    Text(titulo, fontWeight = FontWeight.Bold) // Texto del título en negrita

    Image(
        painter = painterResource(imagenId),
        contentDescription = "",
        Modifier.size(300.dp)
    )

    Text(texto)

    // LaunchedEffect: Ejecuta un efecto secundario cuando entra en composición
    // Unit como clave significa que se ejecuta solo una vez
    LaunchedEffect(Unit) {
        if (!isLoading && texto.isEmpty()) { // Condición: solo ejecutar si no está cargando y el texto está vacío
            isLoading = true // Marcar como cargando
            try {
                val model = Firebase.ai(backend = GenerativeBackend.googleAI()) // Configurar el modelo de IA de Firebase (Gemini)
                    .generativeModel("gemini-2.5-flash-lite")

                val response = model.generateContent(pregunta) // Generar contenido usando el modelo de IA
                texto = response.text.toString() // Almacenar el texto de respuesta
            } catch (e: Exception) {
                texto = textoAlt // En caso de error, usar el texto alternativo
            } finally {
                isLoading = false // Siempre marcar como no cargando, haya éxito o error
            }
        }
    }
}