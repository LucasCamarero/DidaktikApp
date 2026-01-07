package com.lucascamarero.didaktikapp.screens.activities


import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.plus

enum class AnimacionSelecionada {
    Mima,
    Tren,
    Barco,
    Cargadero
}
val ordenCorrecto = listOf(
    AnimacionSelecionada.Mima,
    AnimacionSelecionada.Tren,
    AnimacionSelecionada.Cargadero,
    AnimacionSelecionada.Barco
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Activity6Screen(navController: NavController){
    var animacionActual by remember{ mutableStateOf(setOf<AnimacionSelecionada>()) }
    var pasoActual by remember { mutableStateOf(0) }
    var mensajeError by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()
    fun manejoBotones(animacion: AnimacionSelecionada){
        if(ordenCorrecto[pasoActual] == animacion){
            animacionActual = animacionActual + animacion
            pasoActual ++
            mensajeError = false
            if(pasoActual == ordenCorrecto.size){
                scope.launch {
                    delay(5000)
                    navController.navigate("finActividad/${R.drawable.cargaderos_antigua}/${R.drawable.cargaderos_antigua}")
                }
            }
        }else{
            animacionActual = emptySet()
            pasoActual = 0
            mensajeError = true
        }
    }
    Scaffold(
        bottomBar = {
            Row(Modifier.fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                IconButton({
                    manejoBotones(AnimacionSelecionada.Barco)
                }) { Icon(
                    painter = painterResource(R.drawable.boton_barco),
                    contentDescription = "Barco",
                    tint = Color.Unspecified
                ) }
                IconButton({
                    manejoBotones(AnimacionSelecionada.Mima)
                }) { Icon(
                    painter = painterResource(R.drawable.boton_mina),
                    contentDescription = "Mina",
                    tint = Color.Unspecified
                ) }
                IconButton({
                    manejoBotones(AnimacionSelecionada.Cargadero)
                }) { Icon(
                    painter = painterResource(R.drawable.boton_cargadero),
                    contentDescription = "Cagradero",
                    tint = Color.Unspecified
                ) }
                IconButton({
                    manejoBotones(AnimacionSelecionada.Tren)
                }) { Icon(
                    painter = painterResource(R.drawable.boton_tren),
                    contentDescription = "Tren",
                    tint = Color.Unspecified
                ) }
            }
        }
    ) {
        val lottieTren by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.trenv9)
        )
        val lottieCagradero by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.cargaderoanimacion)
        )
        val lottieBarco by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.animacionbarco)
        )
        val lottieMina by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.animacionmina)
        )
        val infiniteTransition = rememberInfiniteTransition()
        // Tamaño total del área animada
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        // Convertimos a px porque Offset usa px
        val density = LocalDensity.current
        val maxX = with(density) { screenWidth.toPx() }
        val maxY = with(density) { screenHeight.toPx() }
        // Movimiento diagonal: avanza X e Y al mismo ritmo
        val offsetAnim = infiniteTransition.animateFloat(
            initialValue = -900f,
            targetValue = maxX,   // misma distancia para simular 45°
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.fondof2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            if(mensajeError){
                Box(Modifier.fillMaxWidth()
                    .background(Color.Red.copy(0.8f))
                    .padding(8.dp)
                    .align (Alignment.TopCenter)){
                    Text("Orden incorrecto",
                        color = Color.White)
                }
            }
            if (animacionActual.contains(AnimacionSelecionada.Mima)){
                LottieAnimation(
                    composition = lottieMina,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .height(350.dp)
                        .align(Alignment.TopStart)
                        .offset(x = (-50).dp)
                )
            }
            if(animacionActual.contains(AnimacionSelecionada.Tren)){
                DosLineasDiagonalParalelas(maxX)
                // Animación en diagonal
                LottieAnimation(
                    composition = lottieTren,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .width(450.dp)
                        .graphicsLayer {
                            // Movimiento en X = movimiento en Y —> diagonal 45°
                            translationX = (offsetAnim.value).toFloat()
                            translationY = (offsetAnim.value).toFloat()
                            rotationZ = 15f
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.minaf2),
                    contentDescription = null,
                    modifier = Modifier
                        .height(350.dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.FillHeight
                )
            }
            if(animacionActual.contains(AnimacionSelecionada.Cargadero)){
                LottieAnimation(
                    composition = lottieCagradero,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        //.size(150.dp)
                        .height(650.dp)
                        .align(Alignment.BottomEnd)
                )
            }
            if(animacionActual.contains(AnimacionSelecionada.Barco)){
                LottieAnimation(
                    composition = lottieBarco,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        //.size(150.dp)
                        .align(Alignment.BottomStart)
                        .height(350.dp)
                )
            }
        }
    }
}
@Composable
fun DosLineasDiagonalParalelas(maxX:Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Primera diagonal ↘︎
        drawLine(
            color = Color.Black,
            start = Offset(-900f, -900f),
            end = Offset(maxX, maxX),
            strokeWidth = 20f
        )
        // Segunda diagonal ↘︎ desplazada
        drawLine(
            color = Color.Black,
            start = Offset(-1020f, -900f),
            end = Offset(maxX, maxX+120),
            strokeWidth = 20f
        )
    }
}