package com.lucascamarero.didaktikapp.screens.activities.commons

import android.content.pm.ActivityInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.CreateTitle
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.models.ActivityDataSource
import com.lucascamarero.didaktikapp.models.ActivityData
import kotlinx.coroutines.launch

/**
 * Pantalla inicial de una actividad (juego).
 *
 * Muestra el título, una imagen o imágenes,
 * un mensaje introductorio del personaje Jolín y un botón para iniciar el juego.
 *
 * La pantalla está forzada a orientación vertical y utiliza un diseño
 * desplazable para adaptarse a diferentes tamaños de pantalla.
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param activityNumber Identificador numérico de la actividad a cargar.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartOfActivityScreen(
    navController: NavController,
    activityNumber: Int
) {
    /**
     * Datos de la actividad obtenidos desde el origen de datos
     * en función del número de actividad.
     */
    val data = ActivityDataSource.getActivityData(activityNumber)

    /**
     * Indica si el texto del mensaje de Jolín ha finalizado completamente.
     * Controla la visibilidad del botón de inicio.
     */
    var isJolinTextComplete by remember { mutableStateOf(false) }

    /**
     * Lista de recursos de imagen asociados a la actividad.
     * Algunas actividades incluyen varias imágenes para mostrarse en carrusel.
     */
    val images = when (activityNumber) {
        1 -> listOf(R.drawable.juego11_inicio, R.drawable.juego12_inicio)
        2 -> listOf(R.drawable.juego21_inicio)
        3 -> listOf(R.drawable.juego31_inicio)
        4 -> listOf(R.drawable.juego41_inicio)
        5 -> listOf(R.drawable.juego51_inicio)
        6 -> listOf(R.drawable.juego61_inicio)
        7 -> listOf(R.drawable.juego71_inicio)
        8 -> listOf(R.drawable.premio11, R.drawable.premio12)
        else -> listOf(data.imageResId)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        // Contenedor vertical desplazable que aloja todos los elementos
        // de la pantalla en orden secuencial.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            CreateTitle(data.title)

            Spacer(modifier = Modifier.height(20.dp))

            PolaroidImage(
                data = data,
                imageResIds = images
            )

            if (isJolinTextComplete) {
                Spacer(modifier = Modifier.height(26.dp))

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

/**
 * Componente que muestra una imagen o carrusel de imágenes
 * con estilo visual tipo Polaroid.
 *
 * Incluye navegación manual mediante flechas laterales y
 * animaciones de desplazamiento entre imágenes.
 *
 * @param data Información de la actividad asociada a las imágenes.
 * @param imageResIds Lista de identificadores de recursos de imagen.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PolaroidImage(
    data: ActivityData,
    imageResIds: List<Int>
) {
    /**
     * Número total de páginas del carrusel.
     */
    val pageCount = imageResIds.size

    /**
     * Scope de corrutinas utilizado para animar el cambio de página.
     */
    val scope = rememberCoroutineScope()

    /**
     * Estado del paginador horizontal.
     */
    val pagerState = rememberPagerState(initialPage = 0) {
        pageCount
    }

    Box(
        modifier = Modifier
            .size(280.dp, 250.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
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

                IconButton(
                    onClick = {
                        scope.launch {
                            val prevPage =
                                if (pagerState.currentPage > 0) pagerState.currentPage - 1
                                else pageCount - 1
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
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = {
                        scope.launch {
                            val nextPage =
                                if (pagerState.currentPage < pageCount - 1) pagerState.currentPage + 1
                                else 0
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
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}