package com.lucascamarero.didaktikapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
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

@SuppressLint("ResourceType")
@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }
    var isJolinTextComplete by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Espacio superior
        Spacer(modifier = Modifier.height(32.dp))


        // Selector de idioma
        if (!languageIsSelected) {
            LanguageCard { lang ->
                when (lang) {
                    "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
                    "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
                    "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
                }
                languageIsSelected = true
            }
        }


        // Contenido principal
        if (languageIsSelected) {


            CreateTitle(
                mensaje = stringResource(id = R.string.intro_title)
            )



            Spacer(modifier = Modifier.height(24.dp))



            JolinWelcomeMessage(
                message = stringResource(id = R.string.intro),
                onTextComplete = { isJolinTextComplete = it },
                onStartClick = onStartClick,
                jolinSize = 360.dp,
                bubbleSize = 380.dp,
                jolinOffsetY = 250.dp
            )


            if (isJolinTextComplete) {


                Spacer(modifier = Modifier.height(80.dp))



                CreateButton(
                    texto = stringResource(id = R.string.intro_button),
                    onClick = onStartClick
                )

            }
        }

        // Espacio inferior

        Spacer(modifier = Modifier.height(48.dp))

    }
}