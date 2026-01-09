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
            2 -> ActivityData(
                id = 2,
                title = "La iglesia de San Vicente",
                description = stringResource(id = R.string.texto2_intro),
                imageResId = R.drawable.act2_img1, // Reemplaza con tu imagen
                imageDescription = "Fotografia 2",
                gameRoute = "activity2"
            )
            3 -> ActivityData(
                id = 3,
                title = "El Acertijo del Puente",
                description = stringResource(id = R.string.texto3_intro),
                imageResId = R.drawable.activ3_img1, // Reemplaza con tu imagen
                imageDescription = "Fotografia 3",
                gameRoute = "activity3"
            )

            4 -> ActivityData(
                id = 4,
                title = "El Edificio Ilgner",
                description = stringResource(id = R.string.texto4_intro),
                imageResId = R.drawable.act4_img1, // Reemplaza con tu imagen
                imageDescription = "Fotografia 4",
                gameRoute = "activity4"
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
                imageResId = R.drawable.act6_ferrocarril,
                imageDescription = "Fotografia 6",
                gameRoute = "activity6"
            )
            7 -> ActivityData(
                id = 7,
                title = "Palacio Munoa",
                description = stringResource(id = R.string.texto7_intro),
                imageResId = R.drawable.act7_img1,
                imageDescription = "Fotografia 7",
                gameRoute = "activity7"
            )
            8 -> ActivityData(
                id = 8,
                title = stringResource(R.string.final_name),
                description = stringResource(id = R.string.textofinal_intro),
                imageResId = R.drawable.premio11,
                imageDescription = "Fotografía 8",
                gameRoute = "jointhephotos"
            )
            // ... define el resto de las 7 actividades
            else -> throw IllegalArgumentException("Actividad $number no encontrada")
        }
    }
}