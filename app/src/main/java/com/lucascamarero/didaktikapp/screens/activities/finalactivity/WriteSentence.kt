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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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
                text = "Reflexión",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        item {
            Text(
                text = "Ahora piensa en los lugares que has visto en las imágenes.\n\n" +
                        "Escribe una frase explicando por qué es importante cuidar estos lugares y respetar la naturaleza.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 20.dp))
        }

        item {
            Text(
                text = "Ejemplo:\n\"Yo cuidaría ... porque ...\"",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        item {
            OutlinedTextField(
                value = userText,
                onValueChange = { userText = it },
                label = { Text("Escribe tu frase aquí") },
                modifier = Modifier.fillMaxSize()
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 32.dp))
        }

        item {
            Button(
                onClick = {
                    navController.navigate("diploma")
                },
                enabled = userText.isNotBlank()
            ) {
                Text(text = "Obtener diploma")
            }
        }
    }
}