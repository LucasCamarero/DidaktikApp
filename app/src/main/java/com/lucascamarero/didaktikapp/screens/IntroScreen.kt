package com.lucascamarero.didaktikapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateTitle
import com.lucascamarero.didaktikapp.components.CustomGameButton
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.models.AppLanguage
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel

@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }
    var isJolinTextComplete by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

        val isLandscape = maxWidth > maxHeight

        if (!languageIsSelected) {
            // Selección de idioma
            LanguageSelection(
                languageViewModel = languageViewModel,
                onLanguageSelected = { languageIsSelected = true },
                isLandscape = isLandscape
            )
        } else {
            // Pantalla de bienvenida
            IntroContent(
                isJolinTextComplete = isJolinTextComplete,
                onTextComplete = { isJolinTextComplete = it },
                onStartClick = onStartClick
            )
        }
    }
}

@Composable
private fun LanguageSelection(
    languageViewModel: LanguageViewModel,
    onLanguageSelected: () -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageCard(
                isLandscape = isLandscape,
                onLanguageSelected = { lang ->
                    selectLanguage(lang, languageViewModel)
                    onLanguageSelected()
                }
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageCard(
                isLandscape = isLandscape,
                onLanguageSelected = { lang ->
                    selectLanguage(lang, languageViewModel)
                    onLanguageSelected()
                }
            )
        }
    }
}

@Composable
private fun IntroContent(
    isJolinTextComplete: Boolean,
    onTextComplete: (Boolean) -> Unit,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CreateTitle(stringResource(id = R.string.intro_title))

        JolinWelcomeMessage(
            message = stringResource(id = R.string.intro),
            onTextComplete = onTextComplete,
            onStartClick = onStartClick,
            jolinSize = 360.dp,
            bubbleSize = 380.dp,
            jolinOffsetY = 250.dp
        )

        if (isJolinTextComplete) {
            Spacer(modifier = Modifier.height(40.dp))

            CustomGameButton(
                text = stringResource(id = R.string.intro_button),
                backgroundResId = R.drawable.boton_amarillo,
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(68.dp),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

private fun selectLanguage(
    lang: String,
    languageViewModel: LanguageViewModel
) {
    when (lang) {
        "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
        "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
        "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
    }
}
/*
@SuppressLint("ResourceType")
@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }

    // Estado para el texto completo
    var isJolinTextComplete by remember { mutableStateOf(false) }

    if (languageIsSelected == false) {

        /**
         * Determina si la pantalla está en orientación horizontal.
         */
        val isLandscape = maxWidth > maxHeight

        if (isLandscape)
} else {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CreateTitle(
            stringResource(id = R.string.intro_title)
        )

        // Muestra la bienvenida de Jolín, optimizada en una sola función
        JolinWelcomeMessage(
            //message = "",
            message = stringResource(id = R.string.intro),
            onTextComplete = { isJolinTextComplete = it },
            onStartClick = onStartClick
        )

        // Botón de Inicio (solo es visible si el texto de Jolín terminó)
        if (isJolinTextComplete) {
            Spacer(modifier = Modifier.height(40.dp))

            CustomGameButton(
                text = stringResource(id = R.string.intro_button),
                backgroundResId = R.drawable.boton_amarillo,
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
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

@Composable
fun CreateLanguageCard(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit,
    languageIsSelected: Boolean
) {
    LanguageCard { lang ->
        when (lang) {
            "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
            "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
            "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
        }
        languageIsSelected = true
    }
}*/