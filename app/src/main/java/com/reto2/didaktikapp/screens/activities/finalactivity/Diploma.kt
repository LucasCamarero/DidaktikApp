package com.reto2.didaktikapp.screens.activities.finalactivity

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.reto2.didaktikapp.components.CreateTitle2
import com.reto2.didaktikapp.ui.theme.Typography3
import com.reto2.didaktikapp.viewmodels.FinalGameViewModel
import com.reto2.didaktikapp.R

@Composable
fun Diploma(
    navController: NavController,
    viewModel: FinalGameViewModel = viewModel()
) {

    val currentLocale =
        AppCompatDelegate.getApplicationLocales()[0]?.language ?: "eu"

    val diplomaImage = when (currentLocale) {
        "eu" -> R.drawable.diploma_eus
        "en" -> R.drawable.diploma_en
        "es" -> R.drawable.diploma_es
        else -> R.drawable.diploma_eus
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = 20.dp))
        }

        item {
            CreateTitle2(stringResource(R.string.diploma_name))
        }

        item {
            Spacer(modifier = Modifier.padding(top = 25.dp))
        }

        item {
            Image(
                painter = painterResource(id = diplomaImage),
                contentDescription = "Diploma",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 30.dp))
        }

        item {
            Text(
                text = viewModel.userSentence.toUpperCase(),
                style = Typography3.titleLarge.copy(
                    lineHeight = 48.sp
                ),
                color = MaterialTheme.colorScheme.scrim,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}