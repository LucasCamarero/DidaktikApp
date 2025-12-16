package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucascamarero.didaktikapp.ui.theme.Typography3

/**
 * Botón personalizado de la aplicación.
 *
 * Define un botón con:
 * - Altura fija.
 * - Borde visible.
 * - Colores personalizados según el tema.
 * - Tipografía propia para el texto.
 *
 * Este componente encapsula la configuración visual
 * del botón para garantizar consistencia en toda la UI.
 *
 * @param texto Texto que se mostrará dentro del botón.
 * @param onClick Acción que se ejecuta al pulsar el botón.
 * @param modifier Modificador opcional para personalizar
 * el layout externo del botón.
 */
@Composable
fun CreateButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(60.dp),
        border = BorderStroke(
            width = 4.dp,
            color = MaterialTheme.colorScheme.scrim
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.scrim
        ),
        contentPadding = PaddingValues(
            horizontal = 26.dp,
            vertical = 14.dp
        )
    ) {
        Text(
            text = texto,
            style = Typography3.bodyMedium
        )
    }
}