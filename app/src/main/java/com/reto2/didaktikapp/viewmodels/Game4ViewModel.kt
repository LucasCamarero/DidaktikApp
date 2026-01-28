package com.reto2.didaktikapp.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.reto2.didaktikapp.R
import com.reto2.didaktikapp.screens.activities.ToolItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Estado de la pantalla de la Actividad 4.
 *
 * Contiene toda la información necesaria para renderizar la UI:
 * - Herramientas disponibles
 * - Selección actual del usuario
 * - Estado de victoria del juego
 * - Mensaje a mostrar (referencia a recurso de string)
 */
data class GameUiState(
    /**
     * Lista de herramientas que se muestran en el grid.
     */
    val tools: List<ToolItem> = emptyList(),

    /**
     * Conjunto de IDs de herramientas actualmente seleccionadas.
     */
    val selectedIds: Set<Int> = emptySet(),

    /**
     * Indica si el jugador ha completado correctamente el reto.
     */
    val isGameWon: Boolean = false,

    /**
     * Recurso de texto que representa el mensaje actual de la UI.
     *
     * Debe apuntar siempre a un string válido definido en `strings.xml`.
     */
    @StringRes val messageRes: Int = R.string.texto52
)

/**
 * ViewModel encargado de gestionar la lógica de la Actividad 4.
 *
 * Responsabilidades:
 * - Inicializar la lista de herramientas
 * - Gestionar la selección/deselección de objetos
 * - Comprobar si la selección del usuario es correcta
 * - Exponer el estado de la UI mediante un `StateFlow`
 */
@HiltViewModel
class Game4ViewModel @Inject constructor() : ViewModel() {

    /**
     * Estado interno mutable de la pantalla.
     *
     * No debe exponerse directamente fuera del ViewModel.
     */
    private val _uiState = MutableStateFlow(GameUiState())

    /**
     * Estado observable e inmutable de la pantalla.
     *
     * Es consumido por la UI mediante `collectAsState()`.
     */
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    /**
     * Conjunto de IDs de herramientas que constituyen la solución correcta.
     *
     * SOLUCIÓN:
     * - Bombilla (1)
     * - Cables (3)
     * - Generador (5)
     * - Llave (6)
     */
    private val correctToolIds = setOf(1, 3, 5, 6)

    /**
     * Inicializa el ViewModel cargando las herramientas disponibles.
     */
    init {
        loadTools()
    }

    /**
     * Carga la lista inicial de herramientas y la publica en el estado de la UI.
     */
    private fun loadTools() {
        val toolsList = listOf(
            ToolItem(1, "Bombilla", R.drawable.activ4_bombilla),
            ToolItem(2, "Casco", R.drawable.activ4_casco),
            ToolItem(3, "Cables", R.drawable.activ4_cable),
            ToolItem(4, "Martillo", R.drawable.activ4_martillo),
            ToolItem(5, "Generador", R.drawable.activ4_generador),
            ToolItem(6, "Llave", R.drawable.activ4_llave)
        )
        _uiState.update { it.copy(tools = toolsList) }
    }

    /**
     * Marca o desmarca una herramienta al ser pulsada por el usuario.
     *
     * Si el juego ya ha sido ganado, la acción se ignora.
     * Al modificar la selección, el mensaje vuelve al estado inicial.
     *
     * @param id Identificador de la herramienta pulsada
     */
    fun toggleSelection(id: Int) {
        if (_uiState.value.isGameWon) return

        _uiState.update { currentState ->
            val currentSelection = currentState.selectedIds.toMutableSet()
            if (currentSelection.contains(id)) {
                currentSelection.remove(id)
            } else {
                currentSelection.add(id)
            }

            currentState.copy(
                selectedIds = currentSelection,
                messageRes = R.string.texto52
            )
        }
    }

    /**
     * Comprueba si la selección actual del usuario coincide exactamente
     * con la solución correcta.
     *
     * - Si es correcta, marca el juego como ganado y muestra el mensaje de éxito.
     * - Si es incorrecta, muestra un mensaje de error.
     */
    fun checkAnswer() {
        val currentSelection = _uiState.value.selectedIds

        if (currentSelection == correctToolIds) {
            _uiState.update {
                it.copy(
                    isGameWon = true,
                    messageRes = R.string.texto53
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isGameWon = false,
                    messageRes = R.string.texto54
                )
            }
        }
    }
}