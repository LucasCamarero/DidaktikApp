package com.reto2.didaktikapp.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.reto2.didaktikapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- MODELOS DE DATOS ---

/**
 * Enum que representa las clases sociales posibles dentro del juego.
 */
enum class SocialClass { OBREROS, BURGUESES, NONE }

/**
 * Modelo de dato que representa un elemento del juego.
 *
 * @param id identificador único del elemento
 * @param name nombre descriptivo del elemento
 * @param correctClass clase social correcta a la que pertenece
 * @param icon recurso drawable asociado al elemento
 * @param currentClass clase social actual asignada por el usuario
 */
data class Game7Item(
    val id: Int,
    val name: String,
    val correctClass: SocialClass,
    val icon: Int,
    val currentClass: SocialClass = SocialClass.NONE
)

// --- ESTADO DE LA UI ---

/**
 * Estado de la interfaz de usuario para la Actividad 7.
 *
 * Contiene la lista de elementos del juego, información de feedback
 * y el estado de finalización del juego.
 *
 * @param items lista actual de elementos
 * @param feedbackMessageResId id del string resource del mensaje de feedback
 * @param feedbackMessageArgs argumentos dinámicos para el string (ej. número de errores)
 * @param feedbackColor color del mensaje de feedback
 * @param showFeedback indica si debe mostrarse el feedback
 * @param isGameWon indica si el juego ha sido completado correctamente
 */
data class Game7UiState(
    val items: List<Game7Item> = emptyList(),
    val feedbackMessageResId: Int? = null,
    val feedbackMessageArgs: List<Any> = emptyList(),
    val feedbackColor: Color = Color.Transparent,
    val showFeedback: Boolean = false,
    val isGameWon: Boolean = false
)

/**
 * ViewModel encargado de la lógica de la Actividad 7.
 *
 * Gestiona:
 * - la carga inicial de elementos
 * - el estado de clasificación de cada objeto
 * - la validación del juego
 * - el feedback visual para el usuario
 *
 * ⚠️ El ViewModel NO construye textos finales.
 * Solo expone identificadores de recursos y argumentos.
 */
@HiltViewModel
class Game7ViewModel @Inject constructor() : ViewModel() {

    /**
     * Estado interno mutable de la UI.
     */
    private val _uiState = MutableStateFlow(Game7UiState())

    /**
     * Estado observable expuesto a la UI.
     */
    val uiState: StateFlow<Game7UiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    /**
     * Carga inicial de los elementos del juego.
     *
     * Inicializa la lista con los objetos disponibles y su clase social correcta.
     */
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

    /**
     * Se ejecuta cuando el usuario suelta un objeto en una zona de clasificación.
     *
     * Actualiza la clase social asignada al elemento y oculta cualquier
     * feedback previo.
     *
     * @param itemId identificador del elemento soltado
     * @param targetClass clase social destino
     */
    fun onItemDropped(itemId: Int, targetClass: SocialClass) {
        _uiState.update { state ->
            val updatedList = state.items.map { item ->
                if (item.id == itemId) item.copy(currentClass = targetClass) else item
            }
            state.copy(
                items = updatedList,
                showFeedback = false
            )
        }
    }

    /**
     * Se ejecuta cuando el usuario toca un objeto ya clasificado
     * para devolverlo a la cinta transportadora.
     *
     * @param itemId identificador del elemento a retirar
     */
    fun onItemRemoved(itemId: Int) {
        onItemDropped(itemId, SocialClass.NONE)
    }

    /**
     * Lógica del botón "COMPROBAR".
     *
     * Verifica:
     * 1. Si todos los objetos han sido clasificados
     * 2. Si existen errores de clasificación
     *
     * Actualiza el feedback y el estado del juego según el resultado.
     */
    fun checkGame() {
        val currentItems = _uiState.value.items

        // 1. Verificar si faltan objetos por clasificar
        val isComplete = currentItems.none { it.currentClass == SocialClass.NONE }
        if (!isComplete) {
            updateFeedback(
                messageResId = R.string.texto78,
                args = emptyList(),
                color = Color(0xFFE65100)
            )
            return
        }

        // 2. Verificar errores de clasificación
        val errors = currentItems.filter { it.currentClass != it.correctClass }

        if (errors.isEmpty()) {
            // ÉXITO
            updateFeedback(
                messageResId = R.string.texto79,
                args = emptyList(),
                color = Color(0xFF2E7D32)
            )
            _uiState.update { it.copy(isGameWon = true) }
        } else {
            // ERRORES
            updateFeedback(
                messageResId = R.string.texto80,
                args = listOf(errors.size),
                color = Color(0xFFC62828)
            )

            // Devolver automáticamente los erróneos a la cinta
            val fixedList = currentItems.map { item ->
                if (item.currentClass != item.correctClass)
                    item.copy(currentClass = SocialClass.NONE)
                else
                    item
            }
            _uiState.update { it.copy(items = fixedList) }
        }
    }

    /**
     * Actualiza el mensaje de feedback mostrado al usuario.
     *
     * @param messageResId identificador del recurso string
     * @param args argumentos dinámicos del mensaje
     * @param color color asociado al mensaje
     */
    private fun updateFeedback(
        messageResId: Int,
        args: List<Any>,
        color: Color
    ) {
        _uiState.update {
            it.copy(
                feedbackMessageResId = messageResId,
                feedbackMessageArgs = args,
                feedbackColor = color,
                showFeedback = true
            )
        }
    }
}