package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.ui.theme.Typography3

@Composable
fun CreateTitle(
    message: String
){
    Box {
        // Contorno negro
        Text(
            text = stringResource(id = R.string.intro_title),
            style = Typography3.titleLarge.copy(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                drawStyle = Stroke(width = 20f),
                lineHeight = Typography3.titleLarge.fontSize * 1.3
            )
        )

        // Relleno amarillo
        Text(
            text = stringResource(id = R.string.intro_title),
            style = Typography3.titleLarge.copy(
                color = MaterialTheme.colorScheme.secondaryContainer,
                lineHeight = Typography3.titleLarge.fontSize * 1.3
            )
        )
    }
}