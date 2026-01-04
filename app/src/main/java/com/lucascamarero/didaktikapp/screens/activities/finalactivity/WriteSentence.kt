package com.lucascamarero.didaktikapp.screens.activities.finalactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton

@Composable
fun WriteSentence(navController: NavController) {

    var userText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        item {
            Text(
                text = stringResource(R.string.reflexion_titulo),
                style = MaterialTheme.typography.titleSmall
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        item {
            Text(
                text = stringResource(R.string.reflexion_explicacion),
                style = MaterialTheme.typography.bodySmall
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        item {
            Text(
                text = stringResource(R.string.reflexionfinal),
                style = MaterialTheme.typography.bodySmall
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        item {
            OutlinedTextField(
                value = userText,
                onValueChange = { userText = it },
                label = { Text(stringResource(id = R.string.textocaja)) },
                modifier = Modifier.fillMaxSize()
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 32.dp))
        }

        item {
            if (userText.isNotBlank()) {
                CreateButton(
                    texto = stringResource(id = R.string.diploma_button),
                    onClick = {
                        navController.navigate("diploma")
                    }
                )
            }
        }
    }
}