package com.lucascamarero.didaktikapp.components

import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascamarero.didaktikapp.ui.theme.Typography2
import com.lucascamarero.didaktikapp.ui.theme.Typography3

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
            width = 6.dp,
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
