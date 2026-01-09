package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- MODELOS DE DATOS ---
enum class SocialClass { OBREROS, BURGUESES, NONE }

data class Game7Item(
    val id: Int,
    val name: String,
    val correctClass: SocialClass,
    val icon: Int,
    val currentClass: SocialClass = SocialClass.NONE
)

// --- ESTADO DE LA UI ---
data class Game7UiState(
    val items: List<Game7Item> = emptyList(),
    val feedbackMessage: String = "",
    val feedbackColor: Color = Color.Transparent,
    val showFeedback: Boolean = false,
    val isGameWon: Boolean = false // Para saber cuándo navegar
)

@HiltViewModel
class Game7ViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(Game7UiState())
    val uiState: StateFlow<Game7UiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        val initialList = listOf(
            Game7Item(1, "Martillo", SocialClass.OBREROS, R.drawable.activ7_img_martillo),
            Game7Item(2, "Sombrero", SocialClass.BURGUESES, R.drawable.activ7_img_sombrero),
            Game7Item(3, "Fábrica", SocialClass.OBREROS, R.drawable.activ7_img_fabrica),
            Game7Item(4, "Palacio", SocialClass.BURGUESES, R.drawable.activ7_img_banco),
            Game7Item(5, "Pobreza", SocialClass.OBREROS, R.drawable.activ7_img_pobreza),
            Game7Item(6, "Riqueza", SocialClass.BURGUESES, R.drawable.activ7_img_riqueza)
        )
        _uiState.update { it.copy(items = initialList) }
    }

    // --- ACCIONES DEL USUARIO ---

    // Cuando el usuario suelta un objeto en una zona
    fun onItemDropped(itemId: Int, targetClass: SocialClass) {
        _uiState.update { state ->
            val updatedList = state.items.map { item ->
                if (item.id == itemId) item.copy(currentClass = targetClass) else item
            }
            state.copy(
                items = updatedList,
                showFeedback = false // Ocultamos feedback previo al mover ficha
            )
        }
    }

    // Cuando el usuario toca un objeto ya clasificado para devolverlo a la cinta
    fun onItemRemoved(itemId: Int) {
        onItemDropped(itemId, SocialClass.NONE)
    }

    // Lógica del botón COMPROBAR
    fun checkGame() {
        val currentItems = _uiState.value.items

        // 1. Verificar si faltan objetos
        val isComplete = currentItems.none { it.currentClass == SocialClass.NONE }
        if (!isComplete) {
            updateFeedback("¡Faltan objetos por clasificar! 👇", Color(0xFFE65100))
            return
        }

        // 2. Verificar errores
        val errors = currentItems.filter { it.currentClass != it.correctClass }

        if (errors.isEmpty()) {
            // ÉXITO
            updateFeedback("¡EXCELENTE! TODO CORRECTO 🎉", Color(0xFF2E7D32))
            _uiState.update { it.copy(isGameWon = true) }
        } else {
            // ERRORES
            updateFeedback("¡UPS! HAY ${errors.size} ERRORES. CORRÍGELOS 🧐", Color(0xFFC62828))

            // Devolver automáticamente los erróneos a la cinta
            val fixedList = currentItems.map { item ->
                if (item.currentClass != item.correctClass) item.copy(currentClass = SocialClass.NONE) else item
            }
            _uiState.update { it.copy(items = fixedList) }
        }
    }

    private fun updateFeedback(message: String, color: Color) {
        _uiState.update {
            it.copy(
                feedbackMessage = message,
                feedbackColor = color,
                showFeedback = true
            )
        }
    }
}