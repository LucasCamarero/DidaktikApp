package com.lucascamarero.didaktikapp.screens.activities.commons

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.CreateTitle
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.components.LockScreenOrientation
import com.lucascamarero.didaktikapp.models.ActivityDataSource
import com.lucascamarero.didaktikapp.models.ActivityData
import kotlinx.coroutines.launch

// --- Componente de Imagen con Estilo Polaroid y Carrusel ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PolaroidImage(
    data: ActivityData,
    imageResIds: List<Int> // Lista de imágenes para el carrusel
) {
    val pageCount = imageResIds.size
    val scope = rememberCoroutineScope() // Necesario para cambiar de página programáticamente

    val pagerState = rememberPagerState(initialPage = 0) {
        pageCount
    }

    Box(
        modifier = Modifier
            // 💡 1. REDUCIMOS EL TAMAÑO TOTAL DEL POLAROID
            .size(280.dp, 250.dp) // Ejemplo: Reducido de 340x315 a 300x280
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(10.dp), // Reducimos el padding general
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { // Centramos la columna para el carrusel

            // 1. Carrusel de Imágenes con Flechas de Navegación Superpuestas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp) // Mantenemos una altura decente para la imagen
            ) {
                // A. Horizontal Pager (Carrusel)
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(2.dp))
                ) { page ->
                    Image(
                        painter = painterResource(id = imageResIds[page]),
                        contentDescription = "Imagen ${page + 1} de ${data.title}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 2. FLECHA IZQUIERDA
                IconButton(
                    onClick = {
                        scope.launch {
                            // Calcula la página anterior o vuelve a la última si está en la primera
                            val prevPage = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else pageCount - 1
                            pagerState.animateScrollToPage(prevPage)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "Anterior",
                        tint = Color.White.copy(alpha = 0.8f), // Tono para que destaque sobre la imagen
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 3. FLECHA DERECHA
                IconButton(
                    onClick = {
                        scope.launch {
                            // Calcula la página siguiente o vuelve a la primera si está en la última
                            val nextPage = if (pagerState.currentPage < pageCount - 1) pagerState.currentPage + 1 else 0
                            pagerState.animateScrollToPage(nextPage)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Siguiente",
                        tint = Color.White.copy(alpha = 0.8f), // Tono para que destaque sobre la imagen
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
    }
}


// --- PANTALLA PRINCIPAL DE INTRODUCCIÓN AL JUEGO ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartOfActivityScreen(
    navController: NavController,
    activityNumber: Int
) {
    val data = ActivityDataSource.getActivityData(activityNumber)
    var isJolinTextComplete by remember { mutableStateOf(false) }

    val images = when (activityNumber) {
        1 -> listOf(R.drawable.act1_img1, R.drawable.act1_img2)
        2 -> listOf(R.drawable.activ3_img1)
        6 -> listOf(R.drawable.act6_ferrocarril)
        8 -> listOf(R.drawable.premio11, R.drawable.premio12)
        else -> listOf(data.imageResId)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateTitle(data.title)

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LADO IZQUIERDO: Imagen
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        PolaroidImage(data = data, imageResIds = images)
                    }

                    // LADO DERECHO: Texto + Jolín + Botón
                    Box(
                        modifier = Modifier.weight(1.2f).fillMaxHeight(),
                        contentAlignment = Alignment.Center // Centra todo el bloque de Jolín
                    ) {
                        // 1. El bloque completo (Burbuja + Personaje)
                        JolinWelcomeMessage(
                            message = data.description,
                            onTextComplete = { isJolinTextComplete = it },
                            onStartClick = { navController.navigate(data.gameRoute) },
                            jolinSize = 130.dp,
                            bubbleSize = 210.dp,
                            jolinOffsetY = 0.dp
                        )

                        // 2. El BOTÓN (Posicionado manualmente a la izquierda de Jolín)
                        if (isJolinTextComplete) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 20.dp, end = 140.dp), // Ajusta 'end' para moverlo a la izquierda de Jolín
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CreateButton(
                                    texto = stringResource(id = R.string.intro_play_button),
                                    onClick = { navController.navigate(data.gameRoute) }
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // --- DISEÑO VERTICAL (Sin cambios significativos) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateTitle(data.title)
                PolaroidImage(data = data, imageResIds = images)
                Spacer(modifier = Modifier.height(16.dp))

                if (isJolinTextComplete) {
                    CreateButton(
                        texto = stringResource(id = R.string.intro_play_button),
                        onClick = { navController.navigate(data.gameRoute) }
                    )
                }

                JolinWelcomeMessage(
                    message = data.description,
                    onTextComplete = { isJolinTextComplete = it },
                    onStartClick = { navController.navigate(data.gameRoute) }
                )
            }
        }
    }
}