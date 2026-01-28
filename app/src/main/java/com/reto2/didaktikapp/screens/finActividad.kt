package com.reto2.didaktikapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.reto2.didaktikapp.components.CreateButton

/**
 * Pantalla de finalización de una actividad.
 *
 * Muestra un mensaje de nivel completado junto con una comparación
 * visual entre una imagen histórica antigua y su equivalente actual.
 * Incluye un botón para regresar al mapa principal.
 *
 * @param navController controlador de navegación
 * @param fotoAntiguo recurso drawable de la imagen histórica
 * @param fotoActual recurso drawable de la imagen actual
 */
@Composable
fun finActividad(navController: NavController, fotoAntiguo: Int, fotoActual: Int){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Por si las fotos son grandes
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /**
         * Título principal que indica que el nivel ha sido completado.
         */
        Text(
            text = "¡NIVEL COMPLETADO!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        /**
         * Subtítulo descriptivo que refuerza la idea de progreso histórico.
         */
        Text(
            text = "Has desbloqueado la evolución histórica",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // FOTO 1: ANTIGUA
        /**
         * Tarjeta que muestra la imagen histórica antigua.
         */
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    "ANTES (1920)",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Image(
                    painter = painterResource(fotoAntiguo), // PON AQUÍ TU FOTO ANTIGUA
                    contentDescription = "Foto Antigua",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Altura fija para que se vean bien
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FOTO 2: ACTUAL
        /**
         * Tarjeta que muestra la imagen actual del mismo lugar.
         * Incluye el botón de finalización de la ruta.
         */
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    "AHORA (2025)",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Image(
                    painter = painterResource(fotoActual), // PON AQUÍ TU FOTO ACTUAL
                    contentDescription = "Foto Actual",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.height(12.dp))

                /**
                 * Botón que finaliza la ruta y navega de vuelta al mapa principal.
                 */
                CreateButton(
                    texto = "FINALIZAR RUTA",
                    onClick = {
                        navController.navigate("map")
                    }
                )
            }
        }
    }
}
