package com.lucascamarero.didaktikapp.screens.activities.finalactivity

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R

@Composable
fun Diploma(navController: NavController) {

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
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.diploma_name),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(26.dp))

            Image(
                painter = painterResource(id = diplomaImage),
                contentDescription = "Diploma",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}