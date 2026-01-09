import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt
import com.lucascamarero.didaktikapp.R

// --- 1. MODELO DE DATOS ---

enum class SocialClass { OBREROS, BURGUESES, NONE }

data class GameItem(
    val id: Int,
    val name: String,
    val correctClass: SocialClass,
    val icon: Int,
    var currentClass: SocialClass = SocialClass.NONE
)

// --- 2. PANTALLA PRINCIPAL ---

@Composable
fun Activity7Screen() {
    // Datos iniciales
    val initialItems = remember {
        listOf(
            GameItem(1, "Martillo", SocialClass.OBREROS, R.drawable.activ7_img_martillo),
            GameItem(2, "Sombrero", SocialClass.BURGUESES, R.drawable.activ7_img_sombrero),
            GameItem(3, "Fábrica", SocialClass.OBREROS, R.drawable.activ7_img_fabrica),
            GameItem(4, "Palacio", SocialClass.BURGUESES, R.drawable.activ7_img_banco),
            GameItem(5, "Pobreza", SocialClass.OBREROS, R.drawable.activ7_img_pobreza),
            GameItem(6, "Riqueza", SocialClass.BURGUESES, R.drawable.activ7_img_riqueza)
        )
    }

    val gameItems = remember { mutableStateListOf(*initialItems.toTypedArray()) }
    var obrerosZoneBounds by remember { mutableStateOf(Rect.Zero) }
    var burguesesZoneBounds by remember { mutableStateOf(Rect.Zero) }

    // Estado del arrastre (Fantasma)
    var draggedItem by remember { mutableStateOf<GameItem?>(null) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }

    // Estado de los Mensajes (Sin Toast)
    var resultMessage by remember { mutableStateOf("") }
    var resultColor by remember { mutableStateOf(Color.Transparent) }
    var showMessage by remember { mutableStateOf(false) }

    // --- UI PRINCIPAL ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9C4)) // Fondo Crema
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // CENTRADO HORIZONTAL
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // A. ENCABEZADO
            Text(
                text = "Clasificación de elementos",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF7043), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )

            // B. ZONA CENTRAL (COLUMNAS) - USAMOS WEIGHT PARA QUE SEA MÁS ALTO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Esto hace que ocupe todo el espacio vertical disponible (más alto)
                    .zIndex(1f),
                horizontalArrangement = Arrangement.Center // Centrado horizontal
            ) {
                // Columna OBREROS
                DropZone(
                    title = "OBREROS",
                    modifier = Modifier.weight(0.5f).fillMaxHeight(), // Llena el alto disponible
                    titleStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    ),
                    backgroundColor = Color(0xFF42A5F5),
                    borderColor = Color(0xFF1565C0),
                    items = gameItems.filter { it.currentClass == SocialClass.OBREROS },
                    onPositioned = { obrerosZoneBounds = it },
                    onRemoveItem = { item ->
                        val index = gameItems.indexOfFirst { it.id == item.id }
                        if (index != -1) {
                            gameItems[index] = gameItems[index].copy(currentClass = SocialClass.NONE)
                            showMessage = false // Ocultar mensaje si el usuario cambia algo
                        }
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Columna BURGUESES
                DropZone(
                    title = "BURGUESES",
                    modifier = Modifier.weight(0.5f).fillMaxHeight(), // Llena el alto disponible
                    titleStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    ),
                    backgroundColor = Color(0xFFAB47BC),
                    borderColor = Color(0xFF6A1B9A),
                    items = gameItems.filter { it.currentClass == SocialClass.BURGUESES },
                    onPositioned = { burguesesZoneBounds = it },
                    onRemoveItem = { item ->
                        val index = gameItems.indexOfFirst { it.id == item.id }
                        if (index != -1) {
                            gameItems[index] = gameItems[index].copy(currentClass = SocialClass.NONE)
                            showMessage = false
                        }
                    }
                )
            }

            // --- ZONA DE MENSAJES (FEEDBACK) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = showMessage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = resultMessage,
                        color = resultColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // C. CINTA TRANSPORTADORA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFCA28), Color(0xFFFFA000))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(4.dp, Color(0xFFFF6F00), RoundedCornerShape(16.dp))
                    .zIndex(2f),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally), // Centrado items
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    gameItems.forEachIndexed { index, item ->
                        if (item.currentClass == SocialClass.NONE) {
                            DraggableItemSource(
                                item = item,
                                isHidden = draggedItem?.id == item.id,
                                onDragStart = { startPosition ->
                                    draggedItem = item
                                    dragPosition = startPosition
                                    showMessage = false // Ocultar mensaje al mover
                                },
                                onDrag = { dragAmount ->
                                    dragPosition += dragAmount
                                },
                                onDragEnd = {
                                    if (obrerosZoneBounds.contains(dragPosition)) {
                                        gameItems[index] = item.copy(currentClass = SocialClass.OBREROS)
                                    } else if (burguesesZoneBounds.contains(dragPosition)) {
                                        gameItems[index] = item.copy(currentClass = SocialClass.BURGUESES)
                                    }
                                    draggedItem = null
                                }
                            )
                        }
                    }
                }
            }

            // D. BOTÓN COMPROBAR
            Button(
                onClick = {
                    val isComplete = gameItems.none { it.currentClass == SocialClass.NONE }

                    if (!isComplete) {
                        resultMessage = "¡Faltan objetos por clasificar! 👇"
                        resultColor = Color(0xFFE65100) // Naranja oscuro
                        showMessage = true
                        return@Button
                    }

                    val errors = gameItems.filter { it.currentClass != it.correctClass }

                    if (errors.isEmpty()) {
                        // ÉXITO
                        resultMessage = "¡EXCELENTE! TODO CORRECTO 🎉"
                        resultColor = Color(0xFF2E7D32) // Verde oscuro
                        showMessage = true
                    } else {
                        // ERRORES
                        resultMessage = "¡UPS! HAY ${errors.size} ERRORES. CORRÍGELOS 🧐"
                        resultColor = Color(0xFFC62828) // Rojo oscuro
                        showMessage = true

                        // Devolvemos los erróneos a la cinta automáticamente
                        errors.forEach { errorItem ->
                            val index = gameItems.indexOfFirst { it.id == errorItem.id }
                            gameItems[index] = errorItem.copy(currentClass = SocialClass.NONE)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("¡COMPROBAR!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // --- E. CAPA FLOTANTE ---
        if (draggedItem != null) {
            FloatingItemToken(
                item = draggedItem!!,
                position = dragPosition
            )
        }
    }
}

// --- 3. COMPONENTES AUXILIARES ---

@Composable
fun DraggableItemSource(
    item: GameItem,
    isHidden: Boolean,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                currentPosition = coordinates.boundsInWindow().center
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart(currentPosition) },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    }
                )
            }
    ) {
        Box(modifier = Modifier.alpha(if (isHidden) 0f else 1f)) {
            ItemToken(item = item, size = 100.dp)
        }
    }
}

@Composable
fun FloatingItemToken(item: GameItem, position: Offset) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    (position.x - 50.dp.toPx()).roundToInt(),
                    (position.y - 50.dp.toPx()).roundToInt()
                )
            }
            .zIndex(100f),
        contentAlignment = Alignment.Center
    ) {
        ItemToken(item = item, size = 110.dp)
    }
}

@Composable
fun DropZone(
    title: String,
    modifier: Modifier,
    titleStyle: androidx.compose.ui.text.TextStyle,
    backgroundColor: Color,
    borderColor: Color,
    items: List<GameItem>,
    onPositioned: (Rect) -> Unit,
    onRemoveItem: (GameItem) -> Unit
) {
    Box(
        modifier = modifier
            // Nota: fillMaxHeight ya lo pasamos en el modifier desde el padre
            .border(4.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.boundsInWindow())
            }
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(text = title, style = titleStyle, modifier = Modifier.padding(bottom = 12.dp))

            FlowRowLikeColumn(items) { item ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(100.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onRemoveItem(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun ItemToken(item: GameItem, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FlowRowLikeColumn(items: List<GameItem>, content: @Composable (GameItem) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.chunked(1).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                rowItems.forEach { item -> content(item) }
            }
        }
    }
}

@Composable
fun androidx.compose.ui.unit.Dp.toPx(): Float {
    val density = androidx.compose.ui.platform.LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_5"
) 
@Composable
fun MinigamePreview() {
    MaterialTheme {
        Activity7Screen()
    }
}