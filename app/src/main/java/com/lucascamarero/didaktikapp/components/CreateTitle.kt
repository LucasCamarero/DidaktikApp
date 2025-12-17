package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import com.lucascamarero.didaktikapp.ui.theme.Typography3

/**
 * Composable que muestra un título centrado con un efecto de texto contorneado.
 *
 * El efecto se consigue dibujando el mismo texto dos veces:
 * - Una primera capa con un trazo grueso (contorno).
 * - Una segunda capa con el texto relleno.
 *
 * Este componente es útil para destacar títulos principales en la interfaz.
 *
 * @param message Texto que se mostrará como título.
 */
@Composable
fun CreateTitle(
    message: String
) {
    /**
     * Contenedor que ocupa el ancho disponible y centra su contenido.
     */
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        /**
         * Texto de contorno.
         *
         * Se dibuja primero y utiliza un [Stroke] para simular el borde del texto.
         */
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = Typography3.titleLarge.copy(
                color = MaterialTheme.colorScheme.scrim,
                drawStyle = Stroke(width = 20f),
                lineHeight = Typography3.titleLarge.fontSize * 1.3
            )
        )

        /**
         * Texto de relleno.
         *
         * Se superpone al texto de contorno para completar el efecto visual.
         */
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = Typography3.titleLarge.copy(
                color = MaterialTheme.colorScheme.secondaryContainer,
                lineHeight = Typography3.titleLarge.fontSize * 1.3
            )
        )
    }
}