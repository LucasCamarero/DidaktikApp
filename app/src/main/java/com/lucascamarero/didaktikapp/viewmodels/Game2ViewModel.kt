package com.lucascamarero.didaktikapp.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Modelo de datos que representa una fase del juego.
 *
 * @property id Identificador único de la fase.
 * @property imageRes Recurso drawable de la imagen asociada a la fase.
 * @property correctWordRes Recurso string de la palabra correcta.
 * @property optionsRes Lista de recursos string que representan las opciones disponibles.
 */
data class PhaseData(
    val id: Int,
    val imageRes: Int,
    @StringRes val correctWordRes: Int,
    val optionsRes: List<Int>
)

/**
 * ViewModel que gestiona la lógica del juego de la Actividad 2.
 *
 * Se encarga de:
 * - Mantener el estado de la fase actual
 * - Validar las respuestas del usuario
 * - Controlar el avance entre fases
 * - Exponer mensajes de feedback a la UI
 */
@HiltViewModel
class Game2ViewModel @Inject constructor() : ViewModel() {

    /**
     * Lista de fases que componen el juego.
     *
     * Cada fase define:
     * - Imagen a mostrar
     * - Palabra correcta
     * - Opciones disponibles (mezcladas)
     */
    private val phases = listOf(
        PhaseData(
            1,
            R.drawable.activ2_img_altar,
            R.string.texto26,
            listOf(
                R.string.texto26,
                R.string.texto27,
                R.string.texto28,
                R.string.texto29
            ).shuffled()
        ),
        PhaseData(
            2,
            R.drawable.activ2_img_coro,
            R.string.texto27,
            listOf(
                R.string.texto30,
                R.string.texto27,
                R.string.texto31,
                R.string.texto32
            ).shuffled()
        ),
        PhaseData(
            3,
            R.drawable.activ2_img_sagrario,
            R.string.texto28,
            listOf(
                R.string.texto28,
                R.string.texto33,
                R.string.texto34,
                R.string.texto26
            ).shuffled()
        ),
        PhaseData(
            4,
            R.drawable.activ2_img_via_crucis,
            R.string.texto29,
            listOf(
                R.string.texto35,
                R.string.texto29,
                R.string.texto36,
                R.string.texto37
            ).shuffled()
        )
    )

    /**
     * Índice de la fase actual dentro de la lista de fases.
     */
    var currentPhaseIndex by mutableStateOf(0)
        private set

    /**
     * Recurso string de la palabra soltada correctamente por el usuario.
     * Será null mientras no haya una respuesta correcta.
     */
    var droppedWordRes by mutableStateOf<Int?>(null)
        private set

    /**
     * Recurso string que representa el mensaje de feedback mostrado al usuario.
     */
    var feedbackMessageRes by mutableStateOf(R.string.texto38)
        private set

    /**
     * Indica si la respuesta actual es correcta.
     */
    var isCorrectAnswer by mutableStateOf(false)
        private set

    /**
     * Indica si el juego ha finalizado (última fase completada).
     */
    var isGameFinished by mutableStateOf(false)
        private set

    /**
     * Devuelve la fase actual de forma segura.
     */
    val currentPhase: PhaseData
        get() = if (currentPhaseIndex < phases.size) phases[currentPhaseIndex] else phases[0]

    /**
     * Comprueba si la palabra seleccionada es correcta para la fase actual.
     *
     * @param selectedWordRes Recurso string de la palabra seleccionada por el usuario.
     */
    fun checkAnswer(selectedWordRes: Int) {
        if (selectedWordRes == currentPhase.correctWordRes) {
            droppedWordRes = selectedWordRes
            isCorrectAnswer = true
            feedbackMessageRes = R.string.texto39
        } else {
            feedbackMessageRes = R.string.texto40
        }
    }

    /**
     * Avanza a la siguiente fase si existe.
     * Si no hay más fases, marca el juego como finalizado.
     */
    fun nextPhase() {
        if (currentPhaseIndex < phases.size - 1) {
            currentPhaseIndex++
            resetPhaseState()
        } else {
            isGameFinished = true
        }
    }

    /**
     * Reinicia el juego desde la primera fase.
     */
    fun restartGame() {
        currentPhaseIndex = 0
        isGameFinished = false
        resetPhaseState()
    }

    /**
     * Restablece el estado de la fase actual.
     */
    private fun resetPhaseState() {
        droppedWordRes = null
        isCorrectAnswer = false
        feedbackMessageRes = R.string.texto38
    }
}