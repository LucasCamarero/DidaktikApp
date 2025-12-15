package com.lucascamarero.didaktikapp.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascamarero.didaktikapp.R // Asegúrate de que R es accesible

/**
 * Botón personalizado con una imagen de fondo (PNG) y texto estilizado.
 * Simula el aspecto de un botón de interfaz de juego casual.
 *
 * @param text El texto a mostrar en el botón (será blanco).
 * @param backgroundResId El ID del recurso drawable (PNG) para el fondo.
 * @param onClick La acción a realizar al hacer clic.
 * @param modifier Modificador para el tamaño y alineación del botón.
 * @param buttonHeight La altura deseada para el botón (ya que la imagen puede ser más pequeña).
 */
@Composable
fun CustomGameButton(
    text: String,
    @DrawableRes backgroundResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 60.dp,
    // Definir el estilo por defecto (el actual)
    textStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.ExtraBold
    )
) {
    // Usamos Box para superponer la imagen de fondo y el texto.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            // Usamos un InteractionSource para que el efecto de pulsación
            // estándar de Compose funcione sobre el área clickable.
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Puedes anular la indicación si el PNG ya tiene su propio feedback visual
            ),
        contentAlignment = Alignment.Center
    ) {
        // 1. Imagen de Fondo (El PNG del botón)
        Image(
            painter = painterResource(id = backgroundResId),
            contentDescription = text,
            modifier = Modifier.fillMaxSize(),
            // Ajustamos la escala para que la imagen cubra todo el Box
            contentScale = ContentScale.Fit
        )

        // 2. Texto elegante de color blanco
        Text(
            text = text,
            color = Color.White, // Color del texto blanco, como solicitaste
            textAlign = TextAlign.Center,
            style = textStyle,
            // Un pequeño padding para que el texto no toque los bordes del botón.
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
    }
}