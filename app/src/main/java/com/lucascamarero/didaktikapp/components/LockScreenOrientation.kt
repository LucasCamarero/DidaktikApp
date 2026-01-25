package com.lucascamarero.didaktikapp.components

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Composable que bloquea temporalmente la orientación de la pantalla.
 *
 * Este componente fuerza una orientación concreta mientras el composable
 * está activo en la jerarquía de composición. Cuando el composable se
 * elimina, la orientación original de la actividad se restaura
 * automáticamente.
 *
 * Se apoya en un [DisposableEffect] para aplicar y revertir el cambio
 * de orientación de forma segura y controlada.
 *
 * @param orientation Orientación de pantalla a forzar, definida mediante
 * constantes de [ActivityInfo] (por ejemplo,
 * [ActivityInfo.SCREEN_ORIENTATION_PORTRAIT]).
 */
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(orientation) {
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = orientation

        onDispose {
            activity?.requestedOrientation =
                originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

