package com.reto2.didaktikapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable que muestra un mensaje final superpuesto a la pantalla.
 *
 * Este componente se utiliza habitualmente como pantalla de cierre
 * de una actividad o juego, mostrando un título, un mensaje descriptivo
 * y un botón de acción para finalizar o continuar el flujo de la aplicación.
 *
 * El fondo se presenta oscurecido para centrar la atención del usuario
 * en el contenido del mensaje.
 *
 * @param titulo Texto que se mostrará como título principal.
 * @param mensaje Texto descriptivo o informativo que acompaña al título.
 * @param botonText Texto que se mostrará en el botón de acción.
 * Por defecto es "FINALIZAR".
 * @param onButtonClick Acción que se ejecuta al pulsar el botón.
 */
@Composable
fun MensajeFinalActivity(
    titulo: String,
    mensaje: String,
    botonText: String = "FINALIZAR", // Valor por defecto
    onButtonClick: () -> Unit // Lambda para pasar la acción de navegar
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)), // Fondo oscurecido
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = titulo,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                // Mensaje
                Text(
                    text = mensaje,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black // Aseguramos que se vea negro
                )

                // Botón
                Button(
                    onClick = onButtonClick, // Aquí ejecutamos la acción que nos pasan
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF154c79))
                ) {
                    Text(
                        text = botonText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
