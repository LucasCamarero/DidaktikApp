package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.ui.theme.Typography3

@Composable
fun CreateTitle(
    mensaje: String
) {
    Box {
        // Contorno negro
        Text(
            text = stringResource(id = R.string.intro_title),
            style = Typography3.titleLarge.copy(
                color = Color.Black,
                drawStyle = Stroke(width = 20f)
            )
        )

        // Relleno amarillo
        Text(
            text = stringResource(id = R.string.intro_title),
            style = Typography3.titleLarge.copy(
                color = Color(0xFFF5DC21)
            )
        )
    }
}