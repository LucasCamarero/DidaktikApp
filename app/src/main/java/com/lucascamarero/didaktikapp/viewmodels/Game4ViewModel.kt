package com.lucascamarero.didaktikapp.viewmodels

import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.screens.activities.ToolItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Estado de la pantalla: Define todo lo que se ve en la UI
data class GameUiState(
    val tools: List<ToolItem> = emptyList(),
    val selectedIds: Set<Int> = emptySet(), // INICIO: Conjunto vacío, nada seleccionado
    val isGameWon: Boolean = false,
    val message: String = "Elige los objetos que puedan encender el edificio Ilgner."
)

@HiltViewModel
class Game4ViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // SOLUCIÓN: Bombilla(1), Cables(3), Generador(5), Llave(6)
    private val correctToolIds = setOf(1, 3, 5, 6)

    init {
        loadTools()
    }

    private fun loadTools() {
        val toolsList = listOf(
            ToolItem(1, "Bombilla", R.drawable.activ4_bombilla),
            ToolItem(2, "Casco", R.drawable.activ4_casco),
            ToolItem(3, "Cables", R.drawable.activ4_cable),
            ToolItem(4, "Martillo", R.drawable.activ4_martillo),
            ToolItem(5, "Generador", R.drawable.activ4_generador),
            ToolItem(6, "Llave", R.drawable.activ4_llave)
        )
        // Inicializamos la lista de herramientas
        _uiState.update { it.copy(tools = toolsList) }
    }

    // Lógica para marcar/desmarcar
    fun toggleSelection(id: Int) {
        if (_uiState.value.isGameWon) return // Bloqueamos si ya ganó

        _uiState.update { currentState ->
            val currentSelection = currentState.selectedIds.toMutableSet()
            if (currentSelection.contains(id)) {
                currentSelection.remove(id)
            } else {
                currentSelection.add(id)
            }

            // Al tocar algo, reseteamos el mensaje de error al mensaje original
            currentState.copy(
                selectedIds = currentSelection,
                message = "Elige los objetos que puedan encender el edificio Ilgner."
            )
        }
    }

    // Lógica del botón ENCENDER
    fun checkAnswer() {
        val currentSelection = _uiState.value.selectedIds

        // Comparamos si la selección es EXACTAMENTE igual a la correcta
        if (currentSelection == correctToolIds) {
            _uiState.update {
                it.copy(
                    isGameWon = true,
                    message = "¡Correcto! El sistema eléctrico está funcionando."
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isGameWon = false,
                    message = "Objeto incorrecto o faltan herramientas, vuelva a intentarlo."
                )
            }
        }
    }
}