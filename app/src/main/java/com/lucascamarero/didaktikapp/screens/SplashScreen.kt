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
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.lucascamarero.didaktikapp.components.LogoLottie
import kotlinx.coroutines.delay

/**
 * Pantalla de presentación de la aplicación (SplashScreen).
 *
 * Muestra dos animaciones Lottie con un efecto de escala progresiva
 * y un layout adaptable a la orientación del dispositivo:
 * - En orientación vertical, los logotipos se disponen en columna.
 * - En orientación horizontal, los logotipos se disponen en fila.
 *
 * La pantalla se mantiene visible durante la ejecución de la animación
 * principal y, tras un breve retardo adicional, invoca el callback
 * [onTimeout] para continuar el flujo de navegación.
 *
 * Flujo de ejecución:
 * 1. Se inicializa una animación de escala desde un valor mínimo.
 * 2. Se cargan y reproducen dos composiciones Lottie.
 * 3. Se adapta el layout según las dimensiones disponibles.
 * 4. Tras finalizar la animación y el retardo, se ejecuta [onTimeout].
 *
 * @param onTimeout Función callback que se ejecuta al finalizar
 * el tiempo de visualización del SplashScreen.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    /**
     * Animación de escala aplicada a los logotipos.
     */
    val scale = remember { Animatable(0.01f) }

    /**
     * Primera composición Lottie (Txurdinaga).
     */
    val composition1 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            com.lucascamarero.didaktikapp.R.raw.logo_txurdinaga
        )
    )
    val progress1 by animateLottieCompositionAsState(
        composition1,
        iterations = 1
    )

    /**
     * Segunda composición Lottie (UPV/EHU).
     */
    val composition2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            com.lucascamarero.didaktikapp.R.raw.logo_upv
        )
    )
    val progress2 by animateLottieCompositionAsState(
        composition2,
        iterations = 1
    )

    /**
     * Controla la animación de escala y el tiempo de espera
     * antes de continuar la navegación.
     */
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.5f,
            animationSpec = tween(
                durationMillis = 4000,
                easing = LinearOutSlowInEasing
            )
        )
        delay(1500)
        onTimeout()
    }

    /**
     * Contenedor principal que permite adaptar el layout
     * según el tamaño disponible.
     */
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        /**
         * Determina si la pantalla está en orientación horizontal.
         */
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            // Horizontal → logos en fila
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogoLottie(composition1, progress1, scale.value)
                LogoLottie(composition2, progress2, scale.value)
            }
        } else {
            // Vertical → logos en columna
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoLottie(composition1, progress1, scale.value)
                LogoLottie(composition2, progress2, scale.value)
            }
        }
    }
}