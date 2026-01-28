package com.lucascamarero.didaktikapp.screens.activities.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

/**
 * Pantalla final mostrada al completar una actividad.
 *
 * Esta pantalla presenta al usuario un resumen visual del progreso realizado,
 * comparando una imagen del estado inicial con otra del estado final de la actividad.
 * Además, permite registrar la actividad como completada y regresar al mapa principal.
 *
 * @param navController Controlador de navegación para volver a la pantalla del mapa.
 * @param activityId Identificador de la actividad completada.
 * @param imageBeforeRes Imagen Premio antigua.
 * @param imageAfterRes Imagen Premio actual.
 * @param counterViewModel ViewModel encargado de registrar la actividad como completada.
 */
@Composable
fun EndOfActivityScreen(
    navController: NavController,
    activityId: Int,
    imageBeforeRes: Int,
    imageAfterRes: Int,
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
            text = stringResource(id = R.string.textocf1),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.scrim,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.textocf2),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // foto antigua
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Text(stringResource(id = R.string.textocf3), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
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

        // foto actual
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Text(stringResource(id = R.string.textocf4), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
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

        // botón para finalizar
        CreateButton(
            texto = stringResource(id = R.string.textocf5),
            onClick = {
                counterViewModel.marcarActividadComoCompletada(actividadId = activityId)
                navController.navigate("map")
            }
        )
    }
}