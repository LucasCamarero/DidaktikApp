package com.lucascamarero.didaktikapp.screens

import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lucascamarero.didaktikapp.R
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(navController: NavController) {
    val textoJolin =
        "¡Hola, chicas y chicos! ¡Soy Jolín, el personaje más fiestero de Barakaldo! " +
                "Me encanta que estéis aquí, porque juntos vamos a descubrir cómo este lugar tan bonito " +
                "ha pasado de ser un pueblo rural lleno de caseríos a una ciudad moderna y llena de vida. " +
                "A lo largo del camino visitaremos lugares muy especiales, resolveremos juegos y retos, y " +
                "conoceremos historias sorprendentes de nuestro pueblo.\n\n" +
                "¡Preparad vuestros ojos curiosos y vuestras ganas de aprender! Cada vez que superéis " +
                "una prueba, ¡desbloquearéis fotos antiguas y actuales de Barakaldo! Al final del viaje… " +
                "¡os espera una sorpresa y un diploma por convertiros en verdaderos exploradores barakaldeses!\n\n" +
                "¿Listos? ¡Pues venga! ¡Vamos a descubrir Barakaldo, de lo rural a lo moderno!"
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SpeechBubbleWithTypewriterText(
            text = textoJolin,
            fondoTexto = R.drawable.bocadillo_transparente,
            velocidadTexto = 85L
        )
        LottieInfinite(R.raw.jolin)
    }

}
@Composable
fun SpeechBubbleWithTypewriterText(
    text: String,
    @RawRes fondoTexto: Int,
    velocidadTexto: Long = 40L //milisegundos entre las letras
){
// Texto parcial (efecto máquina de escribir)
    var displeyedText by remember{ mutableStateOf("") }
    // Control del scroll
    val scrollState = rememberScrollState()
    // Animación del texto apareciendo
    LaunchedEffect(text){
        text.forEachIndexed { index, _ ->
            displeyedText = text.substring(0,index +1)
            delay(velocidadTexto)
            // Scroll automático hacia abajo a medida que entra el texto
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    Box(Modifier.size(400.dp)

        ){
        Image(
            painter = painterResource(fondoTexto),
            contentDescription = "Fonde del texto",
            Modifier.matchParentSize()
        )
        Box(Modifier.size(400.dp)
            .padding(vertical = 75.dp)
            .padding(horizontal = 75.dp)
            .clip(CircleShape)
            .verticalScroll(scrollState)) {
            Text(
                text = displeyedText,
                Modifier.fillMaxSize()
                    .padding(24.dp),
                style = MaterialTheme.typography.labelSmall

            )
        }
    }
}
@Composable
fun LottieInfinite(@RawRes resId: Int){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progreso by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = progreso
    )
}
