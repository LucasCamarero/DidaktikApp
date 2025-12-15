package com.lucascamarero.didaktikapp.components

import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lucascamarero.didaktikapp.R
import kotlinx.coroutines.delay

/**
 * Muestra el personaje de Jolín con una burbuja de diálogo y texto con efecto máquina de escribir.
 *
 * @param message El mensaje de bienvenida que se mostrará.
 * @param onTextComplete Callback que se llama cuando todo el texto se ha mostrado.
 * @param onStartClick Callback que se llama al pulsar el botón de inicio (solo visible si el texto está completo).
 */
@Composable
fun JolinWelcomeMessage(
    message: String,
    onTextComplete: (Boolean) -> Unit,
    onStartClick: () -> Unit
) {
    var isTextComplete  by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Contenedor BOX para superponer el bocadillo y Jolín
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // A. Bocadillo y Texto (componente tal cual)
            SpeechBubbleWithTypewriterText(
                text = message,
                fondoTexto = R.drawable.bocadillo3,
                velocidadTexto = 0L,
                //velocidadTexto = 45L,
                onTextComplete  = {
                    isTextComplete  = true
                    onTextComplete(true)
                }
            )

            // B. Jolín (Personaje)
            // Usamos .align() y .offset() para moverlo al extremo inferior del bocadillo
            LottieInfinite(
                resId = R.raw.jolin,
                modifier = Modifier
                    .size(280.dp) // Tamaño original de Jolín
                    .align(Alignment.BottomCenter) // Alinea al fondo del Box
                    .offset(x = (-80).dp, y = 190.dp) // Mueve a la izquierda y ligeramente hacia abajo
            )
        }

        // 2. Botón de inicio
        // Colocamos el botón en una Row aparte para alinearlo con Jolín y el centro del layout
        Row(
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp), // Añadimos padding superior
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Centramos la fila
        ){
            // Espacio de relleno para alinear el botón con la derecha de Jolín o el bocadillo
            Spacer(modifier = Modifier.size(100.dp))
        }
    }
}

/**
 * Componente que muestra una burbuja de diálogo con texto que aparece letra por letra.
 * Es una versión limpia de la función original, ahora encapsulada aquí.
 */
@Composable
fun SpeechBubbleWithTypewriterText(
    text: String,
    @RawRes fondoTexto: Int,
    velocidadTexto: Long = 40L, //milisegundos entre las letras
    onTextComplete: () -> Unit = {} // callback opcional cuando termina
){
    var displayedText by remember{ mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(text){
        text.forEachIndexed { index, _ ->
            displayedText = text.substring(0,index +1)
            delay(velocidadTexto)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
        onTextComplete()
    }
    Box(
        Modifier.size(width = 350.dp, height = 230.dp),
                contentAlignment = Alignment.Center // Centra el contenido (texto) sobre la imagen
    ){
        Image(
            painter = painterResource(fondoTexto),
            contentDescription = "Fondo del texto",
            contentScale = ContentScale.Fit, // Asegura que la imagen se vea bien
            //modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier
                .matchParentSize()
                //.size(400.dp)
                .padding(vertical = 15.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = displayedText,
                Modifier.fillMaxSize()
                    .padding(14.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}

/**
 * Componente Lottie que reproduce una animación en bucle infinito.
 */
@Composable
fun LottieInfinite(
    @RawRes resId: Int,
    // AÑADIMOS ESTE PARÁMETRO
    modifier: Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progreso by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    LottieAnimation(
        composition = composition,
        progress = { progreso },
        // USAMOS EL MODIFICADOR PASADO AQUÍ
        modifier = modifier
    )
}