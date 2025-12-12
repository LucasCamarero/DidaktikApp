package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // IMPORTANTE
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.data.repositories.ProgresoRepository // Tu repositorio
import dagger.hilt.android.lifecycle.HiltViewModel // IMPORTANTE
import kotlinx.coroutines.launch // IMPORTANTE
import javax.inject.Inject // IMPORTANTE
import kotlin.math.hypot

// ... (Tu data class GameItem se queda igual) ...
data class GameItem(
    val id: Int,
    val imageRes: Int,
    var currentPosition: Offset = Offset.Zero,
    val targetPosition: Offset,
    val isLocked: Boolean = false
)

@HiltViewModel // 1. Anotación para Hilt
class GameViewModel @Inject constructor(
    private val repository: ProgresoRepository // 2. Inyectamos el repo
) : ViewModel() {

    // ... (Tus variables de estado: items, statusText, etc. se quedan igual) ...
    val items = mutableStateListOf<GameItem>()
    var statusText by mutableStateOf("Arrastra los elementos a su posición original")
    var statusColor by mutableStateOf(Color(0xFF1A3B5D))
    var isDragSuccess by mutableStateOf(false)
    var isQuizMode by mutableStateOf(false)
    var isRewardUnlocked by mutableStateOf(false)
    var selectedQuizOption by mutableStateOf(0)

    // DATOS DE SESIÓN (TEMPORALES)
    // En el futuro, esto vendrá de un "UserSessionManager" o DataStore.
    // Por ahora simulamos que el usuario con ID = 1 está jugando la Actividad ID = 1
    private val currentUserId = 1
    private val currentActivityId = 1

    init {
        // ... (Tu init con las coordenadas se queda igual) ...
        items.add(GameItem(id = 1, imageRes = R.drawable.activ1_cruz, targetPosition = Offset(280f, 550f)))
        items.add(GameItem(id = 2, imageRes = R.drawable.activ1_vaca, targetPosition = Offset(150f, 1000f)))
        items.add(GameItem(id = 3, imageRes = R.drawable.activ1_campesino, targetPosition = Offset(700f, 1000f)))
        distributeItemsInBottomRow()
    }

    private fun distributeItemsInBottomRow() {
        val totalWidth = 1000f
        val startY = 1500f
        val itemCount = items.size
        val segmentWidth = totalWidth / itemCount
        items.forEachIndexed { index, item ->
            val centerX = (segmentWidth * index) + (segmentWidth / 2) - 100f
            items[index] = item.copy(currentPosition = Offset(centerX, startY))
        }
    }

    fun updateItemPosition(id: Int, dragAmount: Offset) {
        if (isDragSuccess) return
        val index = items.indexOfFirst { it.id == id }
        if (index != -1 && !items[index].isLocked) {
            val item = items[index]
            val newPosition = item.currentPosition + dragAmount
            items[index] = item.copy(currentPosition = newPosition)
        }
    }

    fun onMainButtonClick() {
        if (isRewardUnlocked) {
            // Lógica para salir
        } else if (!isDragSuccess) {
            validateDrag()
        } else if (!isQuizMode) {
            startQuiz()
        } else {
            validateQuizAnswer()
        }
    }

    private fun validateDrag() {
        var correctCount = 0
        val tolerance = 200.0
        items.forEachIndexed { index, item ->
            val distance = hypot(
                (item.currentPosition.x - item.targetPosition.x).toDouble(),
                (item.currentPosition.y - item.targetPosition.y).toDouble()
            )
            if (distance < tolerance) {
                items[index] = item.copy(currentPosition = item.targetPosition, isLocked = true)
                correctCount++
            }
        }
        if (correctCount == items.size) {
            isDragSuccess = true
            statusText = "¡MUY BIEN! Has completado el paisaje."
            statusColor = Color(0xFF2E7D32)
        } else {
            statusText = "FALTAN ÍTEMS O ESTÁN MAL COLOCADOS."
            statusColor = Color(0xFFC62828)
        }
    }

    private fun startQuiz() {
        isQuizMode = true
        statusText = "¿Qué cosas crees que todavía siguen igual?"
        statusColor = Color(0xFF1A3B5D)
    }

    private fun validateQuizAnswer() {
        if (selectedQuizOption == 1) { // Respuesta Correcta
            isRewardUnlocked = true
            statusText = "¡FELICIDADES! Has desbloqueado el recuerdo."
            statusColor = Color(0xFF2E7D32)

            // ====================================================
            // AQUÍ GUARDAMOS EN LA BASE DE DATOS
            // ====================================================
            saveProgressToDatabase()

        } else {
            statusText = "Respuesta incorrecta. Inténtalo de nuevo."
            statusColor = Color(0xFFC62828)
        }
    }

    // Nueva función privada para llamar al repositorio
    private fun saveProgressToDatabase() {
        viewModelScope.launch {
            try {
                // Llamamos a la función que creaste en ProgresoRepository
                repository.markActivityAsCompleted(
                    actividadId = currentActivityId,
                    personaId = currentUserId
                )
                // Opcional: Podrías añadir un log aquí para verificar que se guardó
                println("BD: Progreso guardado correctamente para Actividad $currentActivityId")
            } catch (e: Exception) {
                println("BD: Error guardando progreso: ${e.message}")
            }
        }
    }
}