package com.lucascamarero.didaktikapp.screens.activities.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CustomGameButton
import com.lucascamarero.didaktikapp.components.JolinWelcomeMessage // ¡Importa Jolín!
import com.lucascamarero.didaktikapp.models.ActivityDataSource
import com.lucascamarero.didaktikapp.models.ActivityData

// --- Componente de Imagen con Estilo Polaroid ---
@Composable
fun PolaroidImage(
    @Suppress("UNUSED_PARAMETER") data: ActivityData // No usada, pero útil si quieres el título abajo
) {
    Box(
        modifier = Modifier
            .size(340.dp, 350.dp) // Tamaño de la polaroid
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White) // Fondo blanco de la polaroid
            .padding(15.dp), // Espacio para el marco
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                painter = painterResource(id = data.imageResId),
                contentDescription = data.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
            // Espacio inferior para el título o descripción de la polaroid
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.imageDescription,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }
    }
}


// --- PANTALLA PRINCIPAL DE INTRODUCCIÓN AL JUEGO ---
@Composable
fun StartOfActivityScreen(
    navController: NavController,
    activityNumber: Int,
    // Eliminamos 'gameRoute' de aquí ya que lo obtenemos del ActivityData
){
    // 1. Obtener los datos de la actividad
    val data = ActivityDataSource.getActivityData(activityNumber)

    // 2. Estado para el texto completo (necesario para mostrar el botón)
    var isJolinTextComplete by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Empezar desde arriba
    ) {
        item {
            // 1. Título de la actividad
            Text(
                text = data.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                // Reducimos el padding superior
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            // Imagen Polaroid
            PolaroidImage(data = data)

            // Botón de Inicio (solo es visible si el texto de Jolín terminó)
            if (isJolinTextComplete) {
                Spacer(modifier = Modifier.height(30.dp))
                //Spacer(modifier = Modifier.height(40.dp))

                CustomGameButton(
                    text = "¡Empezar Juego!",
                    backgroundResId = R.drawable.boton_verde,
                    onClick = {
                        navController.navigate(data.gameRoute) },
                    modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 8.dp)
                        .height(68.dp),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                //Spacer(modifier = Modifier.height(24.dp))
            }

            // Jolín explicando el juego
            JolinWelcomeMessage(
                message = data.description,
                onTextComplete = { isJolinTextComplete = it },
                onStartClick = {
                    // Acción del botón: Navegar a la ruta del juego real
                    navController.navigate(data.gameRoute)
                }
            )
        }
    }
}