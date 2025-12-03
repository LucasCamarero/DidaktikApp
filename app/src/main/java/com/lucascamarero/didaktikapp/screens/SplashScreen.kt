package com.lucascamarero.didaktikapp.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

/**
 * Pantalla de presentación (SplashScreen) que muestra dos animaciones Lottie
 * con un efecto de escala progresiva.
 *
 * La pantalla se muestra durante unos segundos mientras las animaciones
 * se reproducen, y después ejecuta la función [onTimeout].
 *
 * El flujo es el siguiente:
 * 1. Se inicializa una animación de escala desde 0.01f hasta 1.5f.
 * 2. Se reproducen dos animaciones Lottie en secuencia.
 * 3. Tras finalizar la animación principal y esperar 1 segundo,
 *    se invoca [onTimeout] para continuar la navegación.
 *
 * @param onTimeout Función callback que se ejecuta cuando el Splash termina.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Animación de escala inicial
        val scale = remember { Animatable(0.01f) }

        // Primera animación Lottie (Txurdinaga)
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                com.lucascamarero.didaktikapp.R.raw.logo_txurdinaga
            )
        )
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = 1
        )

        // Segunda animación Lottie (UPV/EHU)
        val composition2 by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                com.lucascamarero.didaktikapp.R.raw.logo_upv
            )
        )
        val progress2 by animateLottieCompositionAsState(
            composition2,
            iterations = 1
        )

        // Control de animaciones + tiempo antes de navegar
        LaunchedEffect(Unit) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(
                    durationMillis = 4000, // cuatro segundos
                    easing = LinearOutSlowInEasing
                )
            )
            delay(1500)
            onTimeout()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Animación del logo txurdinaga
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(250.dp)
                    .scale(scale.value)
            )

            // Animación del logo de la UPV/EHU
            LottieAnimation(
                composition = composition2,
                progress = { progress2 },
                modifier = Modifier
                    .size(250.dp)
                    .scale(scale.value)
            )
        }
    }
}