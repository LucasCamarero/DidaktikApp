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

@HiltViewModel
class Game3ViewModel @Inject constructor(
    private val progresoDao: ProgresoDao
) : ViewModel() {

    // Definición de las palabras y sus coordenadas (StartRow, EndRow, StartCol, EndCol)
    val targetWords = listOf(
        // Palabra, StartRow, EndRow, StartCol, EndCol
        WordData("BACALAO", 2, 2, 3, 9), // Fila 2, empieza en Col 3, termina en Col 9 (Horizontal)
        WordData("ALUBIAS", 7, 7, 2, 8),  // Fila 7, empieza en Col 2, termina en Col 8 (Horizontal)
        WordData("PASTEL", 8, 8, 2, 7),   // Fila 8, empieza en Col 2, termina en Col 7 (Horizontal)
        WordData("TALO", 10, 10, 6, 9)  // Fila 10, empieza en Col 6, termina en Col 9 (Horizontal)
    )

    // El grid de letras extraído de la imagen
    val grid = listOf(
        "WYIPNYGHXDT", "IFVWZVFGELZ", "QDJBACALAOF", "IWMMKLCBGEE",
        "ETGDTDNVFRC", "RIVVSIQUOOI", "MRUEHAKAULD", "VTALUBIASLF",
        "CHPASTELTIF", "CDZDRNXZCZB", "WMVNEPTALOT"
    )

    // Estado del juego
    var foundWords = mutableStateListOf<String>() // Se usa .toSet() en la UI
    var isGameFinished by mutableStateOf(false)
    var currentSelection = mutableStateListOf<Pair<Int, Int>>()

    fun onCellTouch(row: Int, col: Int) {
        if (!currentSelection.contains(row to col)) {
            currentSelection.add(row to col)
        }
    }

    fun endSelection(personaId: Int) {
        val selectedWord = currentSelection.joinToString("") { (r, c) -> grid[r][c].toString() }

        // Comprobamos si coincide con alguna palabra objetivo
        val match = targetWords.find { it.word == selectedWord || it.word == selectedWord.reversed() }

        if (match != null && !foundWords.contains(match.word)) {
            foundWords.add(match.word)
            checkIfGameFinished(personaId)
        }

        currentSelection.clear()
    }

    private fun checkIfGameFinished(personaId: Int) {
        if (foundWords.size == targetWords.size) {
            isGameFinished = true
            updateDatabase(personaId)
        }
    }

    private fun updateDatabase(personaId: Int) {
        viewModelScope.launch {
            progresoDao.updateProgresoCompletado(
                actividadId = 2,
                personaId = personaId,
                date = LocalDateTime.now().toString()
            )
        }
    }
}

data class WordData(
    val word: String,
    val startRow: Int,
    val endRow: Int,
    val startCol: Int,
    val endCol: Int
)