package com.lucascamarero.didaktikapp.screens.activities.commons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
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
){
    // 1. Obtener los datos de la actividad
    val data = ActivityDataSource.getActivityData(activityNumber)

    // 2. Estado para el texto completo (necesario para mostrar el botón)
    var isJolinTextComplete by remember { mutableStateOf(false) }

    // Define las imágenes que quieres mostrar para esta actividad
    // Debes tener estos drawables en tu carpeta res/drawable
    val images = when (activityNumber) {
        1 -> listOf(R.drawable.act1_img1, R.drawable.act1_img2) // EJEMPLO
        2 -> listOf(R.drawable.activ3_img1)
        // ... define el resto de las 7 actividades
        else -> listOf(data.imageResId)
    }

    // CAMBIO CLAVE: Usamos Column con verticalScroll para evitar LazyColumn
    // y asegurar que Jolín y el botón se rendericen inmediatamente si hay espacio.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .verticalScroll(rememberScrollState()) // Permite el scroll si no cabe
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 1. Título de la actividad
        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            fontSize = 28.sp, // Tamaño ajustado
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // 2. Imagen Polaroid con Carrusel
        PolaroidImage(data = data, imageResIds = images)

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Botón de Empezar Juego (Debajo de Jolín)
        if (isJolinTextComplete) {

            CreateButton(
                texto = stringResource(id = R.string.intro_play_button),
                onClick = {// NAVEGACIÓN DINÁMICA SEGÚN LA DATA
                    navController.navigate(data.gameRoute)}
            )
        }

        // 3. Jolín explicando el juego (Contiene el bocadillo y el ícono de Play)
        JolinWelcomeMessage(
            message = data.description,
            onTextComplete = { isJolinTextComplete = it },
            onStartClick = {
                navController.navigate(data.gameRoute)
            }
        )
    }
}