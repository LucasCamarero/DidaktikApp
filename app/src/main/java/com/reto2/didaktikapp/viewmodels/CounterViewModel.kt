package com.reto2.didaktikapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar el contador de actividades completadas.
 *
 * Observa el progreso almacenado en la base de datos y expone el número
 * de actividades completadas como un [StateFlow] para su consumo desde Compose.
 */
@HiltViewModel
class CounterViewModel @Inject constructor(
    /**
     * DAO encargado de acceder y modificar los datos de progreso en la base de datos.
     */
    private val progresoDao: com.reto2.didaktikapp.data.db.daos.ProgresoDao
) : ViewModel() {

    /**
     * Identificador de la persona actual.
     *
     * ⚠️ Temporal para pruebas. Debe sustituirse por el ID real obtenido del login.
     */
    private val personaId = 1

    /**
     * Estado observable que representa el número de actividades completadas
     * por la persona actual.
     *
     * Se obtiene directamente de la base de datos y se convierte en [StateFlow]
     * para que Compose pueda reaccionar automáticamente a los cambios.
     */
    val count: StateFlow<Int> = progresoDao.getCountCompletados(personaId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * Marca una actividad como completada en la base de datos.
     *
     * Inserta o actualiza el progreso asociado a la actividad y a la persona
     * actual, almacenando la fecha de finalización.
     *
     * @param actividadId identificador de la actividad completada
     */
    fun marcarActividadComoCompletada(actividadId: Int) {
        viewModelScope.launch {
            val fechaActual = System.currentTimeMillis().toString()
            progresoDao.upsertProgresoCompletado(actividadId, personaId, fechaActual)
        }
    }
}