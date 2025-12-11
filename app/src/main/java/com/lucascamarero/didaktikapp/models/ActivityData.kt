package com.lucascamarero.didaktikapp.models

import androidx.annotation.DrawableRes
import com.lucascamarero.didaktikapp.R

data class ActivityData(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int,
    val gameRoute: String // Ruta del composable del juego real
)

// Ejemplo de datos (debes crear esto para tus 7 actividades)
object ActivityDataSource {
    fun getActivityData(number: Int): ActivityData {
        return when (number) {
            1 -> ActivityData(
                id = 1,
                title = "Aventura Fotográfica",
                description = "¡Bienvenido al primer desafío! Observa atentamente las fotos antiguas, únete a ellas para reconstruir la historia de nuestra ciudad. ¡La memoria es clave!",
                imageResId = R.drawable.act1_premio1, // Reemplaza con tu imagen
                gameRoute = "activity1"
            )
            2 -> ActivityData(
                id = 2,
                title = "El Acertijo del Puente",
                description = "Debes descifrar el mensaje oculto. Completa las frases usando las palabras correctas. Solo así podrás continuar tu misión.",
                imageResId = R.drawable.act1_premio2, // Reemplaza con tu imagen
                gameRoute = "activity2"
            )
            // ... define el resto de las 7 actividades
            else -> throw IllegalArgumentException("Actividad $number no encontrada")
        }
    }
}