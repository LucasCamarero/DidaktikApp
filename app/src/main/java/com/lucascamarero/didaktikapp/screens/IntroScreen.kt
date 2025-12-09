package com.lucascamarero.didaktikapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucascamarero.didaktikapp.components.LanguageCard
import com.lucascamarero.didaktikapp.models.AppLanguage
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel

@Composable
fun IntroScreen(
    languageViewModel: LanguageViewModel,
    onStartClick: () -> Unit
) {
    var languageIsSelected by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(languageIsSelected == false) {
            LanguageCard { lang ->
                when (lang) {
                    "eu" -> languageViewModel.changeLanguage(AppLanguage.EUSKERA)
                    "es" -> languageViewModel.changeLanguage(AppLanguage.CASTELLANO)
                    "en" -> languageViewModel.changeLanguage(AppLanguage.INGLES)
                }
                languageIsSelected = true
            }
        } else {
            // se abre screen manager al hacer on click
            IconButton(onClick = onStartClick) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Abrir menú",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(680.dp)
                )
            }
        }
    }
}