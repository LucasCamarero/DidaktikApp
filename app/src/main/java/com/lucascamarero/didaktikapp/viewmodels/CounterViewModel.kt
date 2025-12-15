package com.lucascamarero.didaktikapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // 1. Añade esta anotación
class CounterViewModel @Inject constructor( // 2. Añade @Inject constructor
    private val progresoDao: com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
) : ViewModel() {


    private val personaId = 1 // TEMPORAL DE PRUEBA, LUEGO REEMPLAZAR POR EL DEL LOGIN

    // Observamos el Flow de la DB y lo convertimos a StateFlow para la UI de Compose
    // Esto calculará el valor automáticamente siempre desde la DB
    val count: StateFlow<Int> = progresoDao.getCountCompletados(personaId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Función para marcar como completado en la DB
    fun marcarActividadComoCompletada(actividadId: Int) {
        viewModelScope.launch {
            val fechaActual = System.currentTimeMillis().toString()
            progresoDao.updateProgresoCompletado(actividadId, personaId, fechaActual)
        }
    }
}