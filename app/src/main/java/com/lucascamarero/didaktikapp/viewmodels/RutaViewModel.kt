package com.lucascamarero.didaktikapp.viewmodels

// Archivo: com.barakaldo.app.presentation.viewmodels/RutaViewModel.kt

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamarero.didaktikapp.data.models.Actividad1UiState
import com.lucascamarero.didaktikapp.data.models.DraggableItem
import com.lucascamarero.didaktikapp.data.models.ItemStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Asume que estás usando Hilt para la Inyección de Dependencias
class RutaViewModel @Inject constructor(
    // Necesitas el Repositorio para guardar el progreso
    private val progresoRepository: ProgresoRepository
) : ViewModel() {

    // 1. ESTADO DEL JUEGO: MutableStateFlow para que los cambios fluyan a la UI
    private val _uiState = MutableStateFlow(
        Actividad1UiState(
            // Inicializamos la lista con los 4 elementos sin colocar
            items = DraggableItem.values().map { ItemStatus(item = it) }
        )
    )
    val uiState: StateFlow<Actividad1UiState> = _uiState.asStateFlow()


    /**
     * FUNCIÓN CLAVE: Recibe el resultado de arrastrar y soltar desde la UI.
     * @param itemId El identificador del objeto que se soltó (e.g., "vaca").
     * @param isCorrect Booleano que indica si la posición de soltado fue la correcta.
     * @param finalPosition La coordenada donde se soltó el objeto.
     */
    fun onItemDropped(itemId: String, isCorrect: Boolean, finalPosition: Offset) {
        // Solo procesamos si la colocación fue correcta
        if (!isCorrect) return

        val currentItems = _uiState.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.item.id == itemId }

        if (index != -1 && !currentItems[index].isPlacedCorrectly) {
            // Actualiza el ítem como colocado correctamente
            currentItems[index] = currentItems[index].copy(
                isPlacedCorrectly = true,
                finalPosition = finalPosition
            )

            // Comprobamos si todos están colocados
            val allPlaced = currentItems.all { it.isPlacedCorrectly }

            if (allPlaced) {
                // Actualiza el estado para reflejar el progreso y mostrar el Quiz
                _uiState.update { currentState ->
                    currentState.copy(
                        items = currentItems,
                        isDragAndDropCompleted = true,
                        showQuiz = true // Desbloquea la pregunta
                    )
                }
            } else {
                // Simplemente actualiza el estado con el nuevo ítem colocado
                _uiState.update { currentState ->
                    currentState.copy(items = currentItems)
                }
            }
        }
    }

    /**
     * Maneja la respuesta de la pregunta tipo test.
     */
    fun submitQuizAnswer(answer: String) {
        // La respuesta correcta es la 'a' según el enunciado ("La ermita sigue en pie")
        val isAnswerCorrect = answer.lowercase() == "a"

        if (isAnswerCorrect) {
            viewModelScope.launch {
                // 3. ACTUALIZAR PROGRESO EN LA DB
                // Usamos un identificador para la zona de Santa Águeda
                progresoRepository.updateProgresoCompletado("SantaAgueda", true)

                // Actualizar UI para mostrar las fotos de recompensa
                _uiState.update { it.copy(showQuiz = false, showReward = true) }
            }
        } else {
            // Opcional: Implementar lógica de reintento o mensaje de error.
        }
    }
}