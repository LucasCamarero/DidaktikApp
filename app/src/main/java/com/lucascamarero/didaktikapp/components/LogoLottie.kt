package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation

/**
 * Composable reutilizable que muestra una animación Lottie
 * aplicada a un logotipo con escala dinámica.
 *
 * Este componente se utiliza principalmente en la pantalla
 * de Splash para representar logotipos animados, permitiendo
 * reutilizar el mismo comportamiento visual en distintos layouts
 * (por ejemplo, en filas o columnas).
 *
 * @param composition Composición Lottie cargada desde recursos.
 * @param progress Progreso de la animación Lottie, en un rango de 0f a 1f.
 * @param scale Factor de escala aplicado al logotipo.
 */
@Composable
fun LogoLottie(
    composition: LottieComposition?,
    progress: Float,
    scale: Float
) {
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .size(220.dp)
            .scale(scale)
    )
}