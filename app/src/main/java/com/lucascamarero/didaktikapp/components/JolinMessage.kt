package com.lucascamarero.didaktikapp.components

import android.annotation.SuppressLint
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.ui.theme.Typography3
import kotlinx.coroutines.delay

/**
 * Composable que muestra al personaje Jolín acompañado de una burbuja de diálogo
 * con texto animado mediante un efecto de máquina de escribir.
 *
 * Este componente se utiliza como mensaje de bienvenida o introducción a un juego,
 * guiando al usuario a través de la interfaz mediante un personaje animado.
 *
 * @param message Texto que se mostrará dentro de la burbuja de diálogo.
 * @param onTextComplete Callback que se ejecuta cuando el texto ha terminado
 * de mostrarse completamente.
 * @param onStartClick Callback previsto para acciones posteriores cuando
 * el texto ha finalizado.
 * @param jolinSize Tamaño del personaje Jolín.
 * @param bubbleSize Tamaño de la burbuja de diálogo.
 * @param jolinOffsetX Desplazamiento horizontal del personaje.
 * @param jolinOffsetY Desplazamiento vertical del personaje.
 */
@SuppressLint("ResourceType")
@Composable
fun JolinWelcomeMessage(
    message: String,
    onTextComplete: (Boolean) -> Unit,
    onStartClick: () -> Unit,
    jolinSize: Dp = 200.dp,
    bubbleSize: Dp = 300.dp,
    jolinOffsetX: Dp = (-80).dp,
    jolinOffsetY: Dp = 100.dp
) {
    var isTextComplete  by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Contenedor BOX para superponer el bocadillo y Jolín
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Bocadillo y Texto
            SpeechBubbleWithTypewriterText(
                text = message,
                fondoTexto = R.drawable.bocadillo3,
                velocidadTexto = 4L,
                bubbleSiz = bubbleSize,
                onTextComplete  = {
                    isTextComplete  = true
                    onTextComplete(true)
                }
            )

            // Jolín (Personaje)
            LottieInfinite(
                resId = R.raw.jolin,
                modifier = Modifier
                    .size(jolinSize)
                    .align(Alignment.BottomCenter)
                    .offset(x = jolinOffsetX, y = jolinOffsetY)
            )
        }
    }
}

/**
 * Composable que muestra una burbuja de diálogo con texto animado
 * que aparece progresivamente carácter a carácter.
 *
 * Se utiliza para simular un efecto de escritura automática,
 * típico en diálogos guiados o narrativos.
 *
 * @param text Texto completo que se mostrará en la burbuja.
 * @param fondoTexto Recurso gráfico utilizado como fondo del bocadillo.
 * @param velocidadTexto Tiempo de espera entre la aparición de cada carácter.
 * @param bubbleSiz Tamaño de la burbuja de diálogo.
 * @param onTextComplete Callback que se ejecuta cuando el texto
 * ha terminado de mostrarse.
 */
@SuppressLint("ResourceType")
@Composable
fun SpeechBubbleWithTypewriterText(
    text: String,
    @RawRes fondoTexto: Int,
    velocidadTexto: Long = 4L, //milisegundos entre las letras
    bubbleSiz: Dp = 300.dp,
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
        Modifier.size(bubbleSiz),
        contentAlignment = Alignment.Center // Centra el contenido (texto) sobre la imagen
    ){
        Image(
            painter = painterResource(fondoTexto),
            contentDescription = "Fondo del texto",
            contentScale = ContentScale.Fit, // Asegura que la imagen se vea bien
            modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier
                .matchParentSize()
                .padding(vertical = 75.dp)
                .verticalScroll(scrollState)

        ) {
            Text(
                text = displayedText,
                Modifier.fillMaxSize()
                    .padding(24.dp),
                style = Typography3.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                )
            )
        }
    }
}

/**
 * Composable que reproduce una animación Lottie en bucle infinito.
 *
 * Este componente encapsula la lógica necesaria para cargar
 * y animar recursos Lottie, permitiendo su reutilización
 * en distintos puntos de la interfaz.
 *
 * @param resId Recurso raw que contiene la animación Lottie.
 * @param modifier Modificador opcional para personalizar
 * el layout del componente.
 */
@Composable
fun LottieInfinite(
    @RawRes resId: Int,
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