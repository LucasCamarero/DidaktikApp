package com.lucascamarero.didaktikapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.CreateTitle
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.models.AppLanguage
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel

/**
 * Pantalla de introducción de la aplicación.
 *
 * Esta pantalla gestiona dos estados principales:
 * - Selección del idioma.
 * - Pantalla de bienvenida con el mensaje introductorio.
 *
 * El diseño se adapta automáticamente a orientación vertical u horizontal.
 *
 * @param languageViewModel ViewModel encargado de gestionar el idioma de la app.
 * @param onStartClick Acción que se ejecuta al pulsar el botón de inicio.
 */
@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    /**
     * Indica si el usuario ya ha seleccionado un idioma.
     */
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }

    /**
     * Indica si el texto del mensaje de bienvenida ha terminado de mostrarse.
     */
    var isJolinTextComplete by rememberSaveable { mutableStateOf(false) }

    /**
     * Contenedor que permite adaptar el diseño en función del tamaño disponible.
     */
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

        val isLandscape = maxWidth > maxHeight

        if (!languageIsSelected) {
            /**
             * Pantalla de selección de idioma.
             */
            LanguageSelection(
                languageViewModel = languageViewModel,
                onLanguageSelected = { languageIsSelected = true },
                isLandscape = isLandscape
            )
        } else {
            /**
             * Contenido de bienvenida tras seleccionar idioma.
             */
            IntroContent(
                isJolinTextComplete = isJolinTextComplete,
                onTextComplete = { isJolinTextComplete = it },
                onStartClick = onStartClick,
                isLandscape = isLandscape
            )
        }
    }
}

/**
 * Composable que muestra la selección de idioma.
 *
 * El diseño cambia dependiendo de si la pantalla está en modo horizontal
 * o vertical.
 *
 * @param languageViewModel ViewModel encargado de cambiar el idioma.
 * @param onLanguageSelected Callback que se ejecuta tras seleccionar un idioma.
 * @param isLandscape Indica si la pantalla está en orientación horizontal.
 */
@Composable
private fun LanguageSelection(
    languageViewModel: LanguageViewModel,
    onLanguageSelected: () -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 190.dp),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 60.dp),
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

/**
 * Cambia el idioma de la aplicación según el código recibido.
 *
 * @param lang Código del idioma seleccionado.
 * @param languageViewModel ViewModel que gestiona el idioma.
 */
fun selectLanguage(
    lang: String,
    languageViewModel: LanguageViewModel
) {
    when (lang) {
        "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
        "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
        "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
    }
}

/**
 * Contenido principal de la pantalla de bienvenida.
 *
 * Muestra el título, el mensaje animado de Jolin y el botón de inicio
 * cuando el texto ha finalizado.
 *
 * El diseño se adapta a la orientación de la pantalla.
 *
 * @param isJolinTextComplete Indica si el texto de Jolin ha finalizado.
 * @param onTextComplete Callback que notifica cuando el texto termina.
 * @param onStartClick Acción a ejecutar al pulsar el botón de inicio.
 * @param isLandscape Indica si la pantalla está en orientación horizontal.
 */
@Composable
private fun IntroContent(
    isJolinTextComplete: Boolean,
    onTextComplete: (Boolean) -> Unit,
    onStartClick: () -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateTitle(stringResource(id = R.string.intro_title))
            }

            if (isJolinTextComplete) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    CreateButton(
                        texto = stringResource(id = R.string.intro_button),
                        onClick = onStartClick
                    )
                }
            }

            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                JolinWelcomeMessage(
                    message = stringResource(id = R.string.intro),
                    onTextComplete = onTextComplete,
                    onStartClick = onStartClick,
                    jolinSize = 240.dp,
                    bubbleSize = 340.dp,
                    jolinOffsetX = -240.dp,
                    jolinOffsetY = -20.dp
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CreateTitle(stringResource(id = R.string.intro_title))

            JolinWelcomeMessage(
                message = stringResource(id = R.string.intro),
                onTextComplete = onTextComplete,
                onStartClick = onStartClick,
                jolinSize = 340.dp,
                bubbleSize = 380.dp,
                jolinOffsetY = 240.dp
            )

            if (isJolinTextComplete) {
                Spacer(modifier = Modifier.height(40.dp))

                CreateButton(
                    texto = stringResource(id = R.string.intro_button),
                    onClick = onStartClick
                )
            }
        }
    }
}