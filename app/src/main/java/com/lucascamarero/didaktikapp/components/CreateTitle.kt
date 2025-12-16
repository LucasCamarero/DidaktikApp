package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.ui.theme.Typography3

@Composable
fun CreateTitle(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Contorno
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

        // Relleno
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