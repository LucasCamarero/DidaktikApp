package com.lucascamarero.didaktikapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel que gestiona la lógica del juego de sopa de letras
 * correspondiente a la Actividad 3.
 *
 * Controla:
 * - El grid de letras
 * - Las palabras objetivo
 * - La selección del usuario
 * - La detección de palabras encontradas
 * - El estado de finalización del juego
 * - La persistencia del progreso en base de datos
 */
@HiltViewModel
class Game3ViewModel @Inject constructor(
    /**
     * DAO encargado de persistir el progreso del usuario
     * cuando la actividad es completada.
     */
    private val progresoDao: ProgresoDao
) : ViewModel() {

    /**
     * Lista de palabras objetivo que el usuario debe encontrar,
     * junto con sus coordenadas dentro del grid.
     *
     * Las coordenadas indican:
     * - startRow / endRow: fila inicial y final
     * - startCol / endCol: columna inicial y final
     */
    val targetWords = listOf(
        WordData("BACALAO", 2, 2, 3, 9),
        WordData("ALUBIAS", 7, 7, 2, 8),
        WordData("PASTEL", 8, 8, 2, 7),
        WordData("TALO", 10, 10, 6, 9)
    )

    /**
     * Grid de letras que compone la sopa de letras.
     *
     * Cada String representa una fila del grid.
     */
    val grid = listOf(
        "WYIPNYGHXDT", "IFVWZVFGELZ", "QDJBACALAOF", "IWMMKLCBGEE",
        "ETGDTDNVFRC", "RIVVSIQUOOI", "MRUEHAKAULD", "VTALUBIASLF",
        "CHPASTELTIF", "CDZDRNXZCZB", "WMVNEPTALOT"
    )

    /**
     * Lista reactiva de palabras ya encontradas por el usuario.
     *
     * Se utiliza como fuente de verdad para:
     * - Marcar palabras como completadas
     * - Detectar el fin del juego
     */
    var foundWords = mutableStateListOf<String>()

    /**
     * Indica si el juego ha sido completado correctamente.
     *
     * Cuando pasa a true, la UI muestra el mensaje final.
     */
    var isGameFinished by mutableStateOf(false)

    /**
     * Lista reactiva de celdas actualmente seleccionadas
     * durante el gesto de arrastre del usuario.
     *
     * Cada par representa una coordenada (row, col).
     */
    var currentSelection = mutableStateListOf<Pair<Int, Int>>()

    /**
     * Registra el toque o arrastre del usuario sobre una celda del grid.
     *
     * Evita duplicados dentro de la selección actual.
     *
     * @param row Fila de la celda tocada
     * @param col Columna de la celda tocada
     */
    fun onCellTouch(row: Int, col: Int) {
        if (!currentSelection.contains(row to col)) {
            currentSelection.add(row to col)
        }
    }

    /**
     * Finaliza la selección actual del usuario y comprueba
     * si la palabra formada coincide con alguna palabra objetivo.
     *
     * Si hay coincidencia:
     * - Se añade la palabra a la lista de encontradas
     * - Se comprueba si el juego ha finalizado
     *
     * @param personaId Identificador del usuario que está jugando
     */
    fun endSelection(personaId: Int) {
        val selectedWord = currentSelection.joinToString("") { (r, c) ->
            grid[r][c].toString()
        }

        val match = targetWords.find {
            it.word == selectedWord || it.word == selectedWord.reversed()
        }

        if (match != null && !foundWords.contains(match.word)) {
            foundWords.add(match.word)
            checkIfGameFinished(personaId)
        }

        currentSelection.clear()
    }

    /**
     * Comprueba si todas las palabras objetivo han sido encontradas.
     *
     * En caso afirmativo:
     * - Marca el juego como finalizado
     * - Actualiza el progreso en la base de datos
     *
     * @param personaId Identificador del usuario
     */
    private fun checkIfGameFinished(personaId: Int) {
        if (foundWords.size == targetWords.size) {
            isGameFinished = true
            updateDatabase(personaId)
        }
    }

    /**
     * Guarda en base de datos que la actividad ha sido completada
     * por el usuario en la fecha y hora actuales.
     *
     * @param personaId Identificador del usuario
     */
    private fun updateDatabase(personaId: Int) {
        viewModelScope.launch {
            progresoDao.upsertProgresoCompletado(
                actividadId = 2,
                personaId = personaId,
                date = LocalDateTime.now().toString()
            )
        }
    }
}

/**
 * Modelo de datos que representa una palabra dentro de la sopa de letras
 * junto con su ubicación exacta en el grid.
 *
 * @property word Palabra objetivo
 * @property startRow Fila inicial
 * @property endRow Fila final
 * @property startCol Columna inicial
 * @property endCol Columna final
 */
data class WordData(
    val word: String,
    val startRow: Int,
    val endRow: Int,
    val startCol: Int,
    val endCol: Int
)
