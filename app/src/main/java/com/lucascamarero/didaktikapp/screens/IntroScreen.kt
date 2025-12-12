package com.lucascamarero.didaktikapp.screens

import android.annotation.SuppressLint
// ... otras importaciones
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CustomGameButton
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage // ¡Importa la nueva función!
import com.lucascamarero.didaktikapp.models.AppLanguage
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel

@SuppressLint("ResourceType")
@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }
    val textoJolin = "¡Hola, chicas y chicos! "

    // 2. Estado para el texto completo (necesario para mostrar el botón)
    var isJolinTextComplete by remember { mutableStateOf(false) }

    // estado para controlar si el texto ha terminado de mostrarse
    var isTextComplete by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(languageIsSelected == false) {
            // Muestra el selector de idioma
            LanguageCard { lang ->
                when (lang) {
                    "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
                    "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
                    "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
                }
                languageIsSelected = true
            }
        } else {
            // Muestra la bienvenida de Jolín, optimizada en una sola función
            JolinWelcomeMessage(
                message = stringResource(id = R.string.intro),
                onTextComplete = {  isJolinTextComplete = it },
                onStartClick = onStartClick
            )

            // Botón de Inicio (solo es visible si el texto de Jolín terminó)
            if (isJolinTextComplete) {
                Spacer(modifier = Modifier.height(40.dp))

                CustomGameButton(
                    text = "¡COMENZAR!",
                    backgroundResId = R.drawable.boton_amarillo,
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 8.dp)
                        .height(68.dp),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}

// **Nota:** Las funciones SpeechBubbleWithTypewriterText y LottieInfinite
// han sido movidas al archivo JolinWelcome.kt
// para evitar duplicidad y errores.