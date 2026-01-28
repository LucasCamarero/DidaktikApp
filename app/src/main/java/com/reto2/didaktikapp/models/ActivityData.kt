package com.reto2.didaktikapp.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.reto2.didaktikapp.R

/**
 * Modelo de datos que representa una actividad principal (juego) de la aplicación.
 *
 * Cada instancia encapsula toda la información necesaria para mostrar
 * una actividad en la interfaz de usuario y para navegar hacia su juego
 * o pantalla asociada.
 *
 * @property id Identificador único de la actividad.
 * @property title Título de la actividad, normalmente obtenido desde recursos de string.
 * @property description Texto descriptivo o introductorio de la actividad.
 * @property imageResId Recurso drawable asociado a la actividad.
 * @property imageDescription Descripción textual de la imagen, usada para accesibilidad.
 * @property gameRoute Ruta de navegación asociada al juego o pantalla de la actividad.
 */
data class ActivityData(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int,
    val imageDescription : String,
    val gameRoute: String
)

/**
 * Fuente centralizada de datos de actividades.
 *
 * Este objeto actúa como un proveedor estático de instancias de [ActivityData],
 * resolviendo los recursos de texto mediante Compose y devolviendo la información
 * correspondiente a cada actividad según su identificador.
 */
object ActivityDataSource {

    /**
     * Devuelve los datos de una actividad en función de su número identificador.
     *
     * Esta función es `@Composable` porque utiliza [stringResource] para resolver
     * textos desde los recursos de la aplicación, lo cual requiere un contexto
     * de composición activo.
     *
     * @param number Número identificador de la actividad solicitada.
     * @return Una instancia de [ActivityData] con la información de la actividad.
     *
     * @throws IllegalArgumentException si no existe una actividad asociada
     * al número proporcionado.
     */
    @Composable
    fun getActivityData(number: Int): ActivityData {
        return when (number) {
            1 -> ActivityData(
                id = 1,
                title = stringResource(R.string.title1),
                description = stringResource(id = R.string.texto1_intro),
                imageResId = R.drawable.juego11_inicio,
                imageDescription = "Fotografia 1",
                gameRoute = "activity1"
            )
            2 -> ActivityData(
                id = 2,
                title = stringResource(R.string.title2),
                description = stringResource(id = R.string.texto2_intro),
                imageResId = R.drawable.juego21_inicio,
                imageDescription = "Fotografia 2",
                gameRoute = "activity2"
            )
            3 -> ActivityData(
                id = 3,
                title = stringResource(R.string.title3),
                description = stringResource(id = R.string.texto3_intro),
                imageResId = R.drawable.juego31_inicio,
                imageDescription = "Fotografia 3",
                gameRoute = "activity3"
            )
            4 -> ActivityData(
                id = 4,
                title = stringResource(R.string.title4),
                description = stringResource(id = R.string.texto4_intro),
                imageResId = R.drawable.juego41_inicio,
                imageDescription = "Fotografia 4",
                gameRoute = "activity4"
            )
            5 -> ActivityData(
                id = 5,
                title = stringResource(R.string.title5),
                description = stringResource(id = R.string.texto5_intro),
                imageResId = R.drawable.juego51_inicio,
                imageDescription = "",
                gameRoute = "activity5"
            )
            6 -> ActivityData(
                id = 6,
                title = stringResource(R.string.title6),
                description = stringResource(id = R.string.texto6_intro),
                imageResId = R.drawable.juego61_inicio,
                imageDescription = "Fotografia 6",
                gameRoute = "activity6"
            )
            7 -> ActivityData(
                id = 7,
                title = stringResource(R.string.title7),
                description = stringResource(id = R.string.texto7_intro),
                imageResId = R.drawable.juego71_inicio,
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
            else -> throw IllegalArgumentException("Actividad $number no encontrada")
        }
    }
}