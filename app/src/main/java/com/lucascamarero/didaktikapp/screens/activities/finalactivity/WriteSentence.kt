package com.lucascamarero.didaktikapp.screens.activities.finalactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.CreateTitle2
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel
import com.lucascamarero.didaktikapp.viewmodels.FinalGameViewModel

/**
 * Pantalla final de escritura de reflexión.
 *
 * El usuario escribe una frase final relacionada con la experiencia del juego.
 * El texto se almacena en el [FinalGameViewModel] y se utiliza posteriormente
 * para generar el diploma final.
 *
 * La interacción con el componente [JolinWelcomeMessage] controla el momento
 * en el que el campo de texto se hace visible.
 *
 * @param navController Controlador de navegación utilizado para acceder al diploma final.
 * @param viewModel ViewModel que mantiene el estado del texto introducido por el usuario.
 */
@Composable
fun WriteSentence(
    navController: NavController,
    viewModel: FinalGameViewModel = viewModel(),
    counterViewModel: CounterViewModel = hiltViewModel()
) {
    // Texto local editable por el usuario, preservado ante cambios de configuración
    var localText by rememberSaveable { mutableStateOf("") }

    // Texto almacenado en el ViewModel (estado global del juego final)
    val userText = viewModel.userSentence

    // Indica si el mensaje inicial ha terminado y se puede mostrar el campo de texto
    var isJolinTextComplete by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.padding(top = 20.dp))
        }

        item {
            // Título de la pantalla de reflexión
            CreateTitle2(stringResource(R.string.reflexion_titulo))
        }

        item {
            Spacer(modifier = Modifier.padding(top = 5.dp))
        }

        item {
            // Mensaje guiado que introduce la actividad de reflexión
            JolinWelcomeMessage(
                message = stringResource(id = R.string.reflexion_explicacion),
                onTextComplete = { isJolinTextComplete = it },
                onStartClick = {
                    navController.navigate("diploma")  // ????????????
                }
            )
        }

        item {
            Spacer(modifier = Modifier.padding(top = 80.dp))
        }

        item {
            // Campo de texto visible únicamente cuando el mensaje ha finalizado
            if (isJolinTextComplete) {
                OutlinedTextField(
                    value = localText,
                    onValueChange = { localText = it },
                    label = { Text(stringResource(id = R.string.textocaja)) },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = MaterialTheme.typography.labelLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,

                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,

                        focusedLabelColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,

                        focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.padding(top = 32.dp))
        }

        item {
            // Botón visible solo si el usuario ha escrito texto válido
            if (localText.isNotBlank()) {
                CreateButton(
                    texto = stringResource(id = R.string.diploma_button),
                    onClick = {
                        viewModel.onSentenceChange(localText)
                        counterViewModel.marcarActividadComoCompletada(actividadId = 8)
                        navController.navigate("diploma")
                    }
                )
            }
        }
    }
}