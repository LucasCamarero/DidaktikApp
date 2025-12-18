package com.lucascamarero.didaktikapp.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lucascamarero.didaktikapp.R


data class ActivityData(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int,
    val imageDescription : String,
    val gameRoute: String // Ruta del composable del juego real
)

// Ejemplo de datos (debes crear esto para tus 7 actividades)
object ActivityDataSource {
    @Composable
    fun getActivityData(number: Int): ActivityData {
        return when (number) {
            1 -> ActivityData(
                id = 1,
                title = "La Ermita de Santa Agueda",
                description = stringResource(id = R.string.texto1_intro),
                imageResId = R.drawable.act1_premio1, // Reemplaza con tu imagen
                imageDescription = "Fotografia 1",
                gameRoute = "activity1"
            )
            3 -> ActivityData(
                id = 3,
                title = "El Acertijo del Puente",
                description = stringResource(id = R.string.texto3_intro),
                imageResId = R.drawable.activ3_img1, // Reemplaza con tu imagen
                imageDescription = "",
                gameRoute = "activity3"
            )
            5 -> ActivityData(
                id = 5,
                title = "Rompecabezas",
                description = stringResource(id = R.string.texto5_intro),
                imageResId = R.drawable.fondopuzzle, // Reemplaza con tu imagen
                imageDescription = "",
                gameRoute = "activity5"
            )
            6 -> ActivityData(
                id = 6,
                title = "El ferrocarril",
                description = stringResource(id = R.string.texto6_intro),
                imageResId = R.drawable.act6_ferrocarril, // Reemplaza con tu imagen
                imageDescription = "",
                gameRoute = "activity6"
            )
            // ... define el resto de las 7 actividades
            else -> throw IllegalArgumentException("Actividad $number no encontrada")
        }
    }
}