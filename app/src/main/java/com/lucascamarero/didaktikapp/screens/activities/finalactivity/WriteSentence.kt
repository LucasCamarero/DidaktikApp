package com.lucascamarero.didaktikapp.screens.activities.finalactivity

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.components.CreateTitle2
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage
import com.lucascamarero.didaktikapp.components.LockScreenOrientation

@Composable
fun WriteSentence(navController: NavController) {

    var userText by remember { mutableStateOf("") }

    var isJolinTextComplete by remember { mutableStateOf(false) }

    // 🔒 BLOQUEO SOLO EN VERTICAL
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

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
            CreateTitle2(stringResource(R.string.reflexion_titulo))
        }

        item {
            Spacer(modifier = Modifier.padding(top = 5.dp))
        }

        item {
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

        // poner bonito el text field
        item {
            if (isJolinTextComplete) {
                OutlinedTextField(
                    value = userText,
                    onValueChange = { userText = it },
                    label = { Text(stringResource(id = R.string.textocaja)) },
                    modifier = Modifier.fillMaxSize()
                )
            }
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