package com.lucascamarero.didaktikapp.screens.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.viewmodels.GameViewModel
import kotlin.math.roundToInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.components.CustomGameButton
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel

@Composable
fun Activity1Screen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel(),
    counterViewModel: CounterViewModel = hiltViewModel(), // Asegúrate de compartir la instancia
    //onNavigateBack: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF0F2F5))) {

        // ===================================================================
        // ESCENARIO 1: PANTALLA DE RECOMPENSA (FINAL DEL JUEGO)
        // ===================================================================
        if (viewModel.isRewardUnlocked) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Por si las fotos son grandes
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

                // FOTO 1: ANTIGUA
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                        Text("ANTES (1920)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.act1_premio1), // PON AQUÍ TU FOTO ANTIGUA
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
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                        Text("AHORA (2025)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.act1_premio2), // PON AQUÍ TU FOTO ACTUAL
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

                CustomGameButton(
                    text = "FINALIZAR RUTA",
                    backgroundResId = R.drawable.boton_azul,

                    onClick = {
                        counterViewModel.marcarActividadComoCompletada(actividadId = 1)
                        //onNavigateBack()
                        navController.navigate("map")
                    },
                    modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 8.dp)
                        .height(68.dp),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // ===================================================================
        // ESCENARIO 2: JUEGO (Drag & Drop + Quiz)
        // ===================================================================
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // HEADER
                //Box(
                //    modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(50.dp),
                //    contentAlignment = Alignment.Center
                //) {
                //    Text(text = "Barakaldo App", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3B5D))
                //}

                // IMAGEN ESCENARIO
                Image(
                    painter = painterResource(id = R.drawable.act1_bg_game2),
                    contentDescription = "Escenario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp, 16.dp, 0.dp)
                        .weight(2f) // Ajustar altura aquí si quieres
                        .clip(RoundedCornerShape(12.dp))
                )

                // TEXTO DE ESTADO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.statusText,
                        color = viewModel.statusColor,
                        fontSize = if(viewModel.isQuizMode) 18.sp else 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // ZONA DINÁMICA (Cajas o Quiz)
                if (!viewModel.isQuizMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(0.5f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.items.forEach { _ ->
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(12.dp)).background(Color(0xFFE0E0E0)))
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .weight(0.8f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuizOption(text = "a) La ermita sigue en pie", id = 1, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 1 }
                        QuizOption(text = "b) Ya no hay montes", id = 2, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 2 }
                        QuizOption(text = "c) No hay fiestas", id = 3, selectedId = viewModel.selectedQuizOption) { viewModel.selectedQuizOption = 3 }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN DE ACCIÓN
                Button(
                    onClick = { viewModel.onMainButtonClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.isDragSuccess && !viewModel.isQuizMode) Color(0xFF4CAF50) else Color(0xFF0088CC)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    val buttonText = when {
                        !viewModel.isDragSuccess -> "COMPROBAR RESPUESTA"
                        !viewModel.isQuizMode -> "CONTINUAR"
                        else -> "COMPROBAR RESPUESTA"
                    }
                    Text(text = buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // ELEMENTOS ARRASTRABLES (SOLO SI NO ESTAMOS EN QUIZ)
            if (!viewModel.isQuizMode) {
                // Targets
                viewModel.items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(item.targetPosition.x.roundToInt(), item.targetPosition.y.roundToInt()) }
                            .size(90.dp)
                            .border(3.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                            .background(Color.Green.copy(alpha = 0.2f), CircleShape)
                    )
                }
                // Objetos
                viewModel.items.forEach { item ->
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = "Objeto",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset { IntOffset(item.currentPosition.x.roundToInt(), item.currentPosition.y.roundToInt()) }
                            .size(90.dp)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    viewModel.updateItemPosition(item.id, dragAmount)
                                }
                            }
                    )
                }
            }
        }
    }
}

// Auxiliar para las opciones (se mantiene igual)
@Composable
fun QuizOption(text: String, id: Int, selectedId: Int, onSelect: () -> Unit) {
    val isSelected = (id == selectedId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFBBDEFB) else Color.White)
            .border(1.dp, if (isSelected) Color(0xFF0088CC) else Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0088CC))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}