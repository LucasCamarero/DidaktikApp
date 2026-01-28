package com.reto2.didaktikapp.screens.activities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reto2.didaktikapp.components.MensajeFinalActivity
import com.reto2.didaktikapp.viewmodels.Game7Item
import com.reto2.didaktikapp.viewmodels.Game7ViewModel
import com.reto2.didaktikapp.viewmodels.SocialClass
import kotlin.math.roundToInt
import com.reto2.didaktikapp.R

/**
 * Pantalla principal de la Actividad 7.
 *
 * Implementa un juego de clasificación por arrastre (drag & drop),
 * donde el usuario debe asignar elementos a su clase social correcta.
 *
 * @param navController controlador de navegación
 * @param viewModel ViewModel que gestiona el estado y la lógica del juego
 */
@Composable
fun Activity7Screen(
    navController: NavController,
    viewModel: Game7ViewModel = hiltViewModel()
) {
    // Recolectamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Coordenadas de las zonas de drop
    var obrerosZoneBounds by remember { mutableStateOf(Rect.Zero) }
    var burguesesZoneBounds by remember { mutableStateOf(Rect.Zero) }

    // Estado del arrastre
    var draggedItem by remember { mutableStateOf<Game7Item?>(null) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        // ===================== CAPA 1: JUEGO =====================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ENCABEZADO
            Text(
                text = stringResource(id = R.string.texto73),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF7043), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // COLUMNAS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .zIndex(1f),
                horizontalArrangement = Arrangement.Center
            ) {

                DropZone(
                    title = stringResource(id = R.string.titulo5_columna1),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    titleStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    backgroundColor = Color(0xFF42A5F5),
                    borderColor = Color(0xFF1565C0),
                    items = uiState.items.filter { it.currentClass == SocialClass.OBREROS },
                    onPositioned = { obrerosZoneBounds = it },
                    onRemoveItem = { viewModel.onItemRemoved(it.id) }
                )

                Spacer(Modifier.width(16.dp))

                DropZone(
                    title = stringResource(id = R.string.titulo5_columna2),
                    modifier = Modifier.weight(0.5f).fillMaxHeight(),
                    titleStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    backgroundColor = Color(0xFFAB47BC),
                    borderColor = Color(0xFF6A1B9A),
                    items = uiState.items.filter { it.currentClass == SocialClass.BURGUESES },
                    onPositioned = { burguesesZoneBounds = it },
                    onRemoveItem = { viewModel.onItemRemoved(it.id) }
                )
            }

            // FEEDBACK
            Column(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = uiState.showFeedback,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    uiState.feedbackMessageResId?.let { resId ->
                        Text(
                            text = stringResource(
                                id = resId,
                                formatArgs = uiState.feedbackMessageArgs.toTypedArray()
                            ),
                            color = uiState.feedbackColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // CINTA TRANSPORTADORA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFCA28), Color(0xFFFFA000))
                        ),
                        RoundedCornerShape(16.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.items.forEach { item ->
                        if (item.currentClass == SocialClass.NONE) {
                            DraggableItemSource(
                                item = item,
                                isHidden = draggedItem?.id == item.id,
                                onDragStart = {
                                    draggedItem = item
                                    dragPosition = it
                                },
                                onDrag = { dragPosition += it },
                                onDragEnd = {
                                    when {
                                        obrerosZoneBounds.contains(dragPosition) ->
                                            viewModel.onItemDropped(item.id, SocialClass.OBREROS)
                                        burguesesZoneBounds.contains(dragPosition) ->
                                            viewModel.onItemDropped(item.id, SocialClass.BURGUESES)
                                    }
                                    draggedItem = null
                                }
                            )
                        }
                    }
                }
            }

            // BOTÓN COMPROBAR
            Button(
                onClick = { viewModel.checkGame() },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.texto74),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ELEMENTO FLOTANTE
        draggedItem?.let {
            FloatingItemToken(item = it, position = dragPosition)
        }

        // MENSAJE FINAL
        if (uiState.isGameWon) {
            MensajeFinalActivity(
                titulo = stringResource(id = R.string.texto75),
                mensaje = stringResource(id = R.string.texto76),
                botonText = stringResource(id = R.string.texto77),
                onButtonClick = {
                    navController.navigate(
                        "endactivity/7/${R.drawable.premio51}/${R.drawable.premio42}"
                    ) {
                        popUpTo("activity7") { inclusive = true }
                    }
                }
            )
        }
    }
}

// ================= COMPONENTES AUXILIARES =================

@Composable
fun DraggableItemSource(
    item: Game7Item,
    isHidden: Boolean,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                currentPosition = it.boundsInWindow().center
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
        Box(Modifier.alpha(if (isHidden) 0f else 1f)) {
            ItemToken(item = item, size = 100.dp)
        }
    }
}

@Composable
fun FloatingItemToken(item: Game7Item, position: Offset) {
    Box(
        modifier = Modifier.offset {
            IntOffset(
                (position.x - 50.dp.toPx()).roundToInt(),
                (position.y - 50.dp.toPx()).roundToInt()
            )
        }.zIndex(100f),
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
    items: List<Game7Item>,
    onPositioned: (Rect) -> Unit,
    onRemoveItem: (Game7Item) -> Unit
) {
    Box(
        modifier = modifier
            .border(4.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .onGloballyPositioned { onPositioned(it.boundsInWindow()) }
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(title, style = titleStyle, modifier = Modifier.padding(bottom = 12.dp))

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
                        painter = painterResource(item.icon),
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
fun ItemToken(item: Game7Item, size: Dp) {
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(item.icon),
            contentDescription = item.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FlowRowLikeColumn(
    items: List<Game7Item>,
    content: @Composable (Game7Item) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        items.chunked(1).forEach { row ->
            Row(horizontalArrangement = Arrangement.Center) {
                row.forEach { content(it) }
            }
        }
    }
}

@Composable
fun Dp.toPx(): Float {
    val density = androidx.compose.ui.platform.LocalDensity.current
    return with(density) { this@toPx.toPx() }
}