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

// =======================================================
// DATA
// =======================================================

data class GameItem(
    val id: Int,
    val imageRes: Int,
    val currentPosition: Offset = Offset.Zero,
    val targetPosition: Offset,
    val isLocked: Boolean = false
)

// =======================================================
// VIEW MODEL
// =======================================================

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: ProgresoRepository
) : ViewModel() {

    // -----------------------------
    // GAME STATE
    // -----------------------------

    val items = mutableStateListOf<GameItem>()

    var statusTextResId by mutableStateOf(R.string.texto11)

    //var statusColor by mutableStateOf(Color(0xFF1A3B5D))
    var statusColor by mutableStateOf(MaterialTheme.colorScheme.background)

    var isDragSuccess by mutableStateOf(false)
    var isQuizMode by mutableStateOf(false)
    var isRewardUnlocked by mutableStateOf(false)
    var selectedQuizOption by mutableStateOf(0)

    // -----------------------------
    // SESSION DATA (TEMPORAL)
    // -----------------------------

    private val currentUserId = 1
    private val currentActivityId = 1

    // -----------------------------
    // INIT
    // -----------------------------

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

    // =======================================================
    // GAME LOGIC
    // =======================================================

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

    fun onMainButtonClick() {
        when {
            isRewardUnlocked -> Unit
            !isDragSuccess -> validateDrag()
            !isQuizMode -> startQuiz()
            else -> validateQuizAnswer()
        }
    }

    // -----------------------------
    // DRAG VALIDATION
    // -----------------------------

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

    // -----------------------------
    // QUIZ
    // -----------------------------

    private fun startQuiz() {
        isQuizMode = true
        statusTextResId = R.string.texto14
        statusColor = Color(0xFF1A3B5D)
    }

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

    // =======================================================
    // PERSISTENCE
    // =======================================================

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
