package com.lucascamarero.didaktikapp.screens.activities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import kotlin.math.absoluteValue


data class BoxInfo(
    //Guarda información de cada espacio del tablero
    val x: Float = 0f, //Posición X en pantalla
    val y: Float = 0f, //Posición Y en pantalla
    val col: Int = -1, //Columna
    val row: Int = -1, // Fila
    var isCorrecto: Boolean = false
)
@Composable
fun Activity5Screen(navController: NavController) {
    val image = ImageBitmap.imageResource(R.drawable.fondopuzzle) //Carga la imagen del rompecabezas desde drawable
    val totalPieces = pieceMapv2.size
    var correctPieces by remember { mutableStateOf(0) } //cuántas piezas están bien colocadas
    var finished by remember { mutableStateOf(false) } //evita navegar varias veces
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (correctPieces == totalPieces && !finished) {
            finished = true
            navController.navigate(
                "finActividad/${R.drawable.cargaderos_antigua}/${R.drawable.ferrocarril_actual}"
            )
        }
        //Crea una matriz 2x2 para guardar las posiciones del tablero cada celda tendrá su x, y, fila y columna.
        val arrayBox=Array(3) { Array(4) { BoxInfo(
        ) } }
        // Tablero arriba (solo marco vacío)
        Column {
            //Doble for = filas y columnas. dibuja cada casilla del tablero
            for (row in 0..arrayBox.size-1) {
                Row{
                    for (col in 0..arrayBox[row].size-1) {
                        Box(
                            modifier = Modifier
                                .size(100.dp) //Casilla de 100x100 dp
                                .alpha(0.5f) //Semi transparente (solo guía visual)
                                //.padding(4.dp)
                                //.clip(RoundedCornerShape(8.dp))
                                //Dibuja solo una parte de la imagen
                                //Cada casilla muestra su fragmento correcto
                                .drawBehind {
                                    drawImage(
                                        image = image,
                                        srcOffset = IntOffset(
                                            col * size.width.toInt(),
                                            row * size.height.toInt()
                                        ),   // desde dónde recortar
                                        srcSize = IntSize(
                                            size.width.toInt(),
                                            size.height.toInt()
                                        ),       // tamaño del recorte
                                        dstSize = IntSize(size.width.toInt(), size.height.toInt())
                                    )
                                }
                                //Guarda posición exacta en pantalla de cada casilla
                                .onGloballyPositioned { coords ->
                                    arrayBox[row][col] = BoxInfo(
                                        coords.positionInWindow().x,
                                        coords.positionInWindow().y,
                                        col,
                                        row,
                                        false
                                    )

                                }

                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Piezas abajo
        Box() {
            listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).shuffled().forEach { piece -> //Crea las piezas mezcladas
                //DraggablePiece(piece)
                //DraggablePieceConCabezas(piece)
                //DraggablePieceMap(piece)
                DraggablePieceMapAgujeros(
                    pieceIndex = piece,
                    image = image,
                    arrayBox = arrayBox,
                    onPieceCorrect = {
                        correctPieces++ //!!
                    }
                )
                /*
                *Llama al composable que:
                Dibuja la pieza
                Permite arrastrarla
                Verifica si encaja */
            }
        }
    }
}
/*
Dibuja una pieza de rompecabezas real, con:
Cabezas
Huecos
Imagen recortada
*/
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
fun DraggablePieceMapAgujeros( pieceIndex: Int, image: ImageBitmap, arrayBox: Array<Array<BoxInfo>>, onPieceCorrect: () -> Unit) { //Pieza arrastrable
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(0.dp) } //Guarda cuánto se movió la pieza
    var offsetY by remember { mutableStateOf(0.dp) } //Guarda cuánto se movió la pieza
    var isTouching by remember { mutableStateOf(false) }

    val shape = pieceMapv2[pieceIndex]

    PuzzlePieceShape(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(100.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount -> //Detecta el arrastre con el dedo
                    change.consume()
                    offsetX += with(density) { dragAmount.x.toDp() } //Mueve la pieza en pantalla
                    offsetY += with(density) { dragAmount.y.toDp() } //Mueve la pieza en pantalla

                }

            }
            .onGloballyPositioned { coords ->
                val canvasRect = coords.boundsInWindow()

                val isCorrect =
                    (canvasRect.topLeft.y - arrayBox[shape.row][shape.column].y).absoluteValue < 10 &&
                            (canvasRect.topLeft.x - arrayBox[shape.row][shape.column].x).absoluteValue < 10

                if (isCorrect) {
                    isTouching = true
                    arrayBox[shape.row][shape.column].isCorrecto = true
                } else {
                    isTouching = false
                    arrayBox[shape.row][shape.column].isCorrecto = false
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
        isTouching
    )
}