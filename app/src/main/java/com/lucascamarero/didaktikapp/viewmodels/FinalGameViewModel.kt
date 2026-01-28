package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.models.PhotoItem

/**
 * ViewModel que gestiona la lógica del juego final de asociación de imágenes.
 *
 * Controla:
 * - la lista de fotos mezcladas
 * - la selección actual
 * - las parejas acertadas
 * - los mensajes de feedback
 * - la frase final introducida por el usuario
 */
class FinalGameViewModel : ViewModel() {

    /**
     * Lista de fotos del juego.
     *
     * Cada pareja comparte el mismo `pairId`.
     * La lista se mezcla aleatoriamente al inicializar el ViewModel.
     */
    val photos: List<PhotoItem> = listOf(
        PhotoItem(1, 1, R.drawable.premio11),
        PhotoItem(2, 1, R.drawable.premio12),
        PhotoItem(3, 2, R.drawable.premio21),
        PhotoItem(4, 2, R.drawable.premio22),
        PhotoItem(5, 3, R.drawable.premio31),
        PhotoItem(6, 3, R.drawable.premio32),
        PhotoItem(7, 4, R.drawable.premio41),
        PhotoItem(8, 4, R.drawable.premio42),
        PhotoItem(9, 5, R.drawable.premio51),
        PhotoItem(10, 5, R.drawable.premio52),
        PhotoItem(11, 6, R.drawable.premio61),
        PhotoItem(12, 6, R.drawable.premio62),
        PhotoItem(13, 7, R.drawable.premio71),
        PhotoItem(14, 7, R.drawable.premio72),
    ).shuffled()

    /**
     * Primera foto seleccionada por el usuario.
     *
     * Se utiliza para comparar con la segunda selección.
     */
    var firstSelected by mutableStateOf<PhotoItem?>(null)
        private set

    /**
     * Conjunto de IDs de las fotos que ya han sido emparejadas correctamente.
     */
    var matched by mutableStateOf(setOf<Int>())
        private set

    /**
     * Mensaje de texto libre (actualmente no utilizado directamente).
     */
    var message by mutableStateOf("")
        private set

    /**
     * Identificador del recurso string que representa el mensaje de feedback
     * tras intentar emparejar dos fotos.
     */
    var messageResId by mutableStateOf<Int?>(null)
        private set

    /**
     * Frase introducida por el usuario al finalizar el juego.
     */
    var userSentence by mutableStateOf("")
        private set

    /**
     * Gestiona el clic sobre una foto.
     *
     * Implementa la lógica de emparejamiento:
     * - primera selección
     * - comprobación de pareja correcta o incorrecta
     * - actualización de estado y mensajes
     *
     * @param photo foto seleccionada
     * @return `true` si el juego ha sido completado (todas las parejas acertadas)
     */
    fun onPhotoClicked(photo: PhotoItem): Boolean {
        if (matched.contains(photo.id)) return false

        if (firstSelected == null) {
            firstSelected = photo
            messageResId = null
            return false
        }

        if (firstSelected!!.id != photo.id) {
            if (firstSelected!!.pairId == photo.pairId) {
                matched = matched + firstSelected!!.id + photo.id
                messageResId = R.string.asocia1
            } else {
                messageResId = R.string.asocia2
            }
            firstSelected = null
        }

        return matched.size == 14
    }

    /**
     * Indica si una foto está actualmente seleccionada.
     *
     * @param photo foto a comprobar
     * @return `true` si es la foto seleccionada
     */
    fun isSelected(photo: PhotoItem): Boolean =
        firstSelected?.id == photo.id

    /**
     * Indica si una foto ya ha sido emparejada correctamente.
     *
     * @param photo foto a comprobar
     * @return `true` si ya pertenece a una pareja correcta
     */
    fun isMatched(photo: PhotoItem): Boolean =
        matched.contains(photo.id)

    /**
     * Actualiza la frase introducida por el usuario al final del juego.
     *
     * @param text nuevo texto introducido
     */
    fun onSentenceChange(text: String) {
        userSentence = text
    }
}
