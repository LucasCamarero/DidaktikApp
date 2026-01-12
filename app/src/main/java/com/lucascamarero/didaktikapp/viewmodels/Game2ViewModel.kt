package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Modelo de datos para cada fase
data class PhaseData(
    val id: Int,
    val imageRes: Int,
    val correctWord: String,
    val options: List<String>
)

@HiltViewModel
class Game2ViewModel @Inject constructor() : ViewModel() {

    // --- DATOS DEL JUEGO ---
    private val phases = listOf(
        PhaseData(1, R.drawable.activ2_img_altar, "Altar", listOf("Altar", "Coro", "Sagrario", "Vía Crucis").shuffled()),
        PhaseData(2, R.drawable.activ2_img_coro, "Coro", listOf("Púlpito", "Coro", "Banco", "Confesionario").shuffled()),
        PhaseData(3, R.drawable.activ2_img_sagrario, "Sagrario", listOf("Sagrario", "Cáliz", "Vela", "Altar").shuffled()),
        PhaseData(4, R.drawable.activ2_img_via_crucis, "Vía Crucis", listOf("Cruz", "Vía Crucis", "Cuadro", "Estatua").shuffled())
    )

    // --- ESTADOS (Observables por la UI) ---
    var currentPhaseIndex by mutableStateOf(0)
        private set

    var droppedWord by mutableStateOf<String?>(null)
        private set

    var feedbackMessage by mutableStateOf("Arrastra la palabra correcta a la imagen.")
        private set

    var isCorrectAnswer by mutableStateOf(false)
        private set

    var isGameFinished by mutableStateOf(false)
        private set

    // Obtener la fase actual de forma segura
    val currentPhase: PhaseData
        get() = if (currentPhaseIndex < phases.size) phases[currentPhaseIndex] else phases[0]

    // --- LÓGICA ---

    fun checkAnswer(word: String) {
        if (word == currentPhase.correctWord) {
            droppedWord = word
            isCorrectAnswer = true
            feedbackMessage = "¡Correcto! Muy bien."
        } else {
            feedbackMessage = "¡Incorrecto! Inténtalo de nuevo."
        }
    }

    fun nextPhase() {
        if (currentPhaseIndex < phases.size - 1) {
            currentPhaseIndex++
            resetPhaseState()
        } else {
            isGameFinished = true
        }
    }

    fun restartGame() {
        currentPhaseIndex = 0
        isGameFinished = false
        resetPhaseState()
    }

    private fun resetPhaseState() {
        droppedWord = null
        isCorrectAnswer = false
        feedbackMessage = "Arrastra la palabra correcta a la imagen."
    }
}