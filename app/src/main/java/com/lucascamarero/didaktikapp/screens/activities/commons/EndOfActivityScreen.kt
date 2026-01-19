package com.lucascamarero.didaktikapp.screens.activities.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

@Composable
fun EndOfActivityScreen(
    navController: NavController,
    activityId: Int, // El ID de la actividad (1, 2, 3...)
    imageBeforeRes: Int, // La foto antigua (R.drawable...)
    imageAfterRes: Int,  // La foto actual (R.drawable...)
    // Inyectamos el CounterViewModel aquí para manejar la lógica de completado
    counterViewModel: CounterViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡NIVEL COMPLETADO!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Has desbloqueado la evolución histórica",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- FOTO 1: ANTIGUA ---
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Text("ANTES", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                Image(
                    painter = painterResource(id = imageBeforeRes),
                    contentDescription = "Foto Antigua",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FOTO 2: ACTUAL ---
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Text("AHORA", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                Image(
                    painter = painterResource(id = imageAfterRes),
                    contentDescription = "Foto Actual",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN FINALIZAR ---
        CreateButton(
            texto = "FINALIZAR RUTA",
            onClick = {
                // Lógica centralizada
                counterViewModel.marcarActividadComoCompletada(actividadId = activityId)
                navController.navigate("map")
            }
        )
    }
}