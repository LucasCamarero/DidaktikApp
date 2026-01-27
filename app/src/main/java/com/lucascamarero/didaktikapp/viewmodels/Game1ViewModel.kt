package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.data.repositories.ProgresoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.hypot

/**
 * Modelo de datos que representa un elemento interactivo del juego.
 *
 * Cada elemento puede ser arrastrado por el usuario hasta una posición objetivo
 * y quedar bloqueado cuando se coloca correctamente.
 *
 * @property id Identificador único del elemento.
 * @property imageRes Recurso drawable que representa visualmente el elemento.
 * @property currentPosition Posición actual del elemento en pantalla.
 * @property targetPosition Posición objetivo donde debe colocarse el elemento.
 * @property isLocked Indica si el elemento ya ha sido colocado correctamente.
 */
data class GameItem(
    val id: Int,
    val imageRes: Int,
    val currentPosition: Offset = Offset.Zero,
    val targetPosition: Offset,
    val isLocked: Boolean = false
)

/**
 * ViewModel que gestiona el estado y la lógica del juego de la actividad.
 *
 * Controla:
 * - La lógica de drag & drop
 * - El flujo del quiz
 * - El estado visual del texto informativo
 * - La persistencia del progreso del usuario
 *
 * No depende de la UI directamente y sobrevive a cambios de configuración.
 *
 * @property repository Repositorio encargado de persistir el progreso del usuario.
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: ProgresoRepository
) : ViewModel() {

    /**
     * Lista observable de elementos del juego.
     * Se utiliza para representar los objetos arrastrables y sus posiciones.
     */
    val items = mutableStateListOf<GameItem>()

    /**
     * Recurso de texto que representa el mensaje actual del estado del juego.
     */
    var statusTextResId by mutableStateOf(R.string.texto11)

    /**
     * Color asociado al estado actual del mensaje mostrado al usuario.
     */
    var statusColor by mutableStateOf(Color(0xFF1A3B5D))

    /**
     * Indica si el usuario ha completado correctamente el ejercicio de arrastre.
     */
    var isDragSuccess by mutableStateOf(false)

    /**
     * Indica si el juego se encuentra en modo quiz.
     */
    var isQuizMode by mutableStateOf(false)

    /**
     * Indica si el usuario ha desbloqueado la recompensa final.
     */
    var isRewardUnlocked by mutableStateOf(false)

    /**
     * Opción seleccionada actualmente en el quiz.
     */
    var selectedQuizOption by mutableStateOf(0)

    /**
     * Identificador del usuario actual (temporal).
     */
    private val currentUserId = 1

    /**
     * Identificador de la actividad actual (temporal).
     */
    private val currentActivityId = 1

    /**
     * Inicialización del estado del juego.
     *
     * Se crean los elementos iniciales y se distribuyen en la parte inferior
     * de la pantalla.
     */
    init {
        items.add(
            GameItem(
                id = 1,
                imageRes = R.drawable.activ1_cruz,
                targetPosition = Offset(280f, 550f)
            )
        )
        items.add(
            GameItem(
                id = 2,
                imageRes = R.drawable.activ1_vaca,
                targetPosition = Offset(150f, 1000f)
            )
        )
        items.add(
            GameItem(
                id = 3,
                imageRes = R.drawable.activ1_campesino,
                targetPosition = Offset(700f, 1000f)
            )
        )

        distributeItemsInBottomRow()
    }

    /**
     * Distribuye los elementos del juego de forma equidistante
     * en la parte inferior de la pantalla.
     */
    private fun distributeItemsInBottomRow() {
        val totalWidth = 1000f
        val startY = 1500f
        val segmentWidth = totalWidth / items.size

        items.forEachIndexed { index, item ->
            val centerX = (segmentWidth * index) + (segmentWidth / 2) - 100f
            items[index] = item.copy(
                currentPosition = Offset(centerX, startY)
            )
        }
    }

    /**
     * Actualiza la posición de un elemento durante una operación de arrastre.
     *
     * @param id Identificador del elemento que se está arrastrando.
     * @param dragAmount Desplazamiento aplicado desde el último evento de drag.
     */
    fun updateItemPosition(id: Int, dragAmount: Offset) {
        if (isDragSuccess) return

        val index = items.indexOfFirst { it.id == id }
        if (index != -1 && !items[index].isLocked) {
            val item = items[index]
            items[index] = item.copy(
                currentPosition = item.currentPosition + dragAmount
            )
        }
    }

    /**
     * Gestiona la acción principal del botón según el estado actual del juego.
     */
    fun onMainButtonClick() {
        when {
            isRewardUnlocked -> Unit
            !isDragSuccess -> validateDrag()
            !isQuizMode -> startQuiz()
            else -> validateQuizAnswer()
        }
    }

    /**
     * Valida si los elementos han sido colocados correctamente
     * dentro de una tolerancia determinada.
     */
    private fun validateDrag() {
        var correctCount = 0
        val tolerance = 200.0

        items.forEachIndexed { index, item ->
            val distance = hypot(
                (item.currentPosition.x - item.targetPosition.x).toDouble(),
                (item.currentPosition.y - item.targetPosition.y).toDouble()
            )

            if (distance < tolerance) {
                items[index] = item.copy(
                    currentPosition = item.targetPosition,
                    isLocked = true
                )
                correctCount++
            }
        }

        if (correctCount == items.size) {
            isDragSuccess = true
            statusTextResId = R.string.texto12
            statusColor = Color(0xFF2E7D32)
        } else {
            statusTextResId = R.string.texto13
            statusColor = Color(0xFFC62828)
        }
    }

    /**
     * Inicia el modo quiz tras completar correctamente el ejercicio de arrastre.
     */
    private fun startQuiz() {
        isQuizMode = true
        statusTextResId = R.string.texto14
        statusColor = Color(0xFF1A3B5D)
    }

    /**
     * Valida la respuesta seleccionada por el usuario en el quiz.
     */
    private fun validateQuizAnswer() {
        if (selectedQuizOption == 1) {
            isRewardUnlocked = true
            statusTextResId = R.string.texto15
            statusColor = Color(0xFF2E7D32)
            saveProgressToDatabase()
        } else {
            statusTextResId = R.string.texto16
            statusColor = Color(0xFFC62828)
        }
    }

    /**
     * Guarda el progreso del usuario en la base de datos.
     */
    private fun saveProgressToDatabase() {
        viewModelScope.launch {
            try {
                repository.markActivityAsCompleted(
                    actividadId = currentActivityId,
                    personaId = currentUserId
                )
            } catch (e: Exception) {
                // Error controlado (log opcional)
                println("BD: Error guardando progreso: ${e.message}")
            }
        }
    }
}