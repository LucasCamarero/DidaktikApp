package com.lucascamarero.didaktikapp.data.models

import androidx.compose.ui.geometry.Offset

// Define los elementos que pueden ser arrastrados
enum class DraggableItem(val id: String) {
    MONTE("monte"),
    CRUZ("cruz"),
    CAMPESINO("campesino"),
    VACA("vaca")
}

// Estado de cada elemento
data class ItemStatus(
    val item: DraggableItem,
    val isPlacedCorrectly: Boolean = false, // TRUE si se soltó en la zona correcta
    val finalPosition: Offset? = null // Posición para "pegar" el objeto
)

// Estado general de la actividad para la UI
data class Actividad1UiState(
    val items: List<ItemStatus>,
    val isDragAndDropCompleted: Boolean = false, // TRUE cuando los 4 están colocados
    val showQuiz: Boolean = false, // TRUE para mostrar la pregunta tipo test
    val showReward: Boolean = false // TRUE para mostrar las fotos de recompensa
)