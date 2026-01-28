package com.lucascamarero.didaktikapp.screens.activities

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.MensajeFinalActivity

/**
 * Enum que representa las animaciones disponibles en la actividad.
 * Cada valor corresponde a un elemento interactivo del juego.
 */
enum class AnimacionSelecionada {
    Mima,
    Tren,
    Barco,
    Cargadero
}

/**
 * Lista que define el orden correcto en el que el usuario debe
 * activar las animaciones para completar el juego con éxito.
 */
val ordenCorrecto = listOf(
    AnimacionSelecionada.Mima,
    AnimacionSelecionada.Tren,
    AnimacionSelecionada.Cargadero,
    AnimacionSelecionada.Barco
)

/**
 * Pantalla principal de la Actividad 6.
 *
 * Contiene la lógica del juego, animaciones Lottie,
 * control de errores y mensaje final de éxito.
 *
 * @param navController controlador de navegación para avanzar a la siguiente pantalla
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Activity6Screen(navController: NavController) {

    // --- ESTADOS ---
    /** Conjunto de animaciones ya activadas correctamente */
    var animacionActual by remember { mutableStateOf(setOf<AnimacionSelecionada>()) }

    /** Índice del paso actual dentro del orden correcto */
    var pasoActual by remember { mutableStateOf(0) }

    /** Indica si debe mostrarse el mensaje de error */
    var mensajeError by remember { mutableStateOf(false) }

    /** Indica si el juego ha finalizado correctamente */
    var juegoTerminado by remember { mutableStateOf(false) } // 💡 NUEVO ESTADO

    // --- LÓGICA DEL JUEGO ---
    /**
     * Gestiona la pulsación de los botones.
     *
     * Comprueba si la animación seleccionada es la correcta
     * según el paso actual. En caso de error, reinicia el estado.
     *
     * @param animacion animación seleccionada por el usuario
     */
    fun manejoBotones(animacion: AnimacionSelecionada) {
        if (ordenCorrecto[pasoActual] == animacion) {
            // Acierto
            animacionActual = animacionActual + animacion
            pasoActual++
            mensajeError = false

            // Verificar si completó la secuencia
            if (pasoActual == ordenCorrecto.size) {
                juegoTerminado = true // 💡 ACTIVAMOS EL FINAL
            }
        } else {
            // Error: Reiniciar
            animacionActual = emptySet()
            pasoActual = 0
            mensajeError = true
            juegoTerminado = false
        }
    }

    Scaffold(
        bottomBar = {
            // BARRA DE BOTONES (Solo visible si no ha terminado el juego para evitar clics extra)
            if (!juegoTerminado) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton({ manejoBotones(AnimacionSelecionada.Barco) }) {
                        Icon(painter = painterResource(R.drawable.boton_barco), contentDescription = "Barco", tint = Color.Unspecified)
                    }
                    IconButton({ manejoBotones(AnimacionSelecionada.Mima) }) {
                        Icon(painter = painterResource(R.drawable.boton_mina), contentDescription = "Mina", tint = Color.Unspecified)
                    }
                    IconButton({ manejoBotones(AnimacionSelecionada.Cargadero) }) {
                        Icon(painter = painterResource(R.drawable.boton_cargadero), contentDescription = "Cargadero", tint = Color.Unspecified)
                    }
                    IconButton({ manejoBotones(AnimacionSelecionada.Tren) }) {
                        Icon(painter = painterResource(R.drawable.boton_tren), contentDescription = "Tren", tint = Color.Unspecified)
                    }
                }
            }
        }
    ) {
        // --- RECURSOS LOTTIE ---
        val lottieTren by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.trenv9))
        val lottieCagradero by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cargaderoanimacion))
        val lottieBarco by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animacionbarco))
        val lottieMina by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animacionmina))

        val infiniteTransition = rememberInfiniteTransition()
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val density = LocalDensity.current
        val maxX = with(density) { screenWidth.toPx() }

        val offsetAnim = infiniteTransition.animateFloat(
            initialValue = -900f,
            targetValue = maxX,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {

            // 1. FONDO
            Image(
                painter = painterResource(R.drawable.fondof2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // 2. MENSAJE DE ERROR
            if (mensajeError) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error.copy(0.8f))
                        .padding(8.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Text(
                        stringResource(id = R.string.texto69),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. ANIMACIONES (Lógica original)
            if (animacionActual.contains(AnimacionSelecionada.Mima)) {
                LottieAnimation(
                    composition = lottieMina,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .height(350.dp)
                        .align(Alignment.TopStart)
                        .offset(x = (-50).dp)
                )
            }
            if (animacionActual.contains(AnimacionSelecionada.Tren)) {
                DosLineasDiagonalParalelas(maxX)
                LottieAnimation(
                    composition = lottieTren,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .width(450.dp)
                        .graphicsLayer {
                            translationX = (offsetAnim.value)
                            translationY = (offsetAnim.value)
                            rotationZ = 15f
                        }
                )
                // Superposición Mina
                Image(
                    painter = painterResource(id = R.drawable.minaf2),
                    contentDescription = null,
                    modifier = Modifier
                        .height(350.dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.FillHeight
                )
            }
            if (animacionActual.contains(AnimacionSelecionada.Cargadero)) {
                LottieAnimation(
                    composition = lottieCagradero,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .height(650.dp)
                        .align(Alignment.BottomEnd)
                )
            }
            if (animacionActual.contains(AnimacionSelecionada.Barco)) {
                LottieAnimation(
                    composition = lottieBarco,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .height(350.dp)
                )
            }

            // ===================================================================
            // 4. MENSAJE DE ÉXITO Y BOTÓN FINALIZAR (Superpuesto)
            // ===================================================================
            if (juegoTerminado) {
                MensajeFinalActivity(
                    titulo = stringResource(id = R.string.texto70),
                    mensaje = stringResource(id = R.string.texto71),
                    botonText = stringResource(id = R.string.texto72),
                    onButtonClick = {
                        // Aquí defines qué hace el botón específicamente para ESTA actividad
                        val ruta = "endactivity/6/${R.drawable.premio61}/${R.drawable.premio52}"
                        navController.navigate(ruta) {
                            popUpTo("activity6") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Dibuja dos líneas diagonales paralelas que simulan las vías del tren.
 *
 * @param maxX valor máximo en el eje X para adaptar el dibujo al ancho de pantalla
 */
@Composable
fun DosLineasDiagonalParalelas(maxX: Float) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(-900f, -900f),
            end = Offset(maxX, maxX),
            strokeWidth = 20f
        )
        drawLine(
            color = Color.Black,
            start = Offset(-1020f, -900f),
            end = Offset(maxX, maxX + 120),
            strokeWidth = 20f
        )
    }
}