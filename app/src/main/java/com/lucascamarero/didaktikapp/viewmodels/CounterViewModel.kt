package com.lucascamarero.didaktikapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import com.lucascamarero.didaktikapp.data.db.entities.ActividadEntity // Asegúrate de importar tu entidad
import com.lucascamarero.didaktikapp.data.db.entities.PersonaEntity   // Asegúrate de importar tu entidad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val progresoDao: ProgresoDao
    // Si tienes DAOs separados para Persona y Actividad, inyéctalos aquí también.
    // Si no, asumo que puedes insertarlos desde algún sitio.
    // Para simplificar, supondré que tienes métodos en tu DAO o DAOs auxiliares.
) : ViewModel() {

    private val personaId = 1

    // Inicializamos datos básicos al arrancar el ViewModel
    init {
        inicializarBaseDeDatos()
    }

    val count: StateFlow<Int> = progresoDao.getCountCompletados(personaId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun marcarActividadComoCompletada(actividadId: Int) {
        viewModelScope.launch {
            val fechaActual = System.currentTimeMillis().toString()
            progresoDao.upsertProgresoCompletado(actividadId, personaId, fechaActual)
        }
    }

    // --- FUNCIÓN TEMPORAL PARA ASEGURAR QUE EXISTEN DATOS ---
    private fun inicializarBaseDeDatos() {
        viewModelScope.launch {
            // Verificar si existe la persona, si no, crearla
            if (!progresoDao.existsPersona(personaId)) {
                // AQUÍ NECESITAS LLAMAR A TU DAO DE PERSONA PARA INSERTAR AL USUARIO 1
                // Ejemplo: personaDao.insert(PersonaEntity(id = 1, nombre = "Estudiante"))
                // Si no tienes ese DAO a mano, tendrás que crearlo o añadir el insert en ProgresoDao
                android.util.Log.w("CounterVM", "¡Falta el Usuario 1 en la DB! El progreso no se guardará.")
            }

            // Verificar actividades
            for (i in 1..7) {
                if (!progresoDao.existsActividad(i)) {
                    // AQUÍ NECESITAS INSERTAR LAS ACTIVIDADES 1 a 7
                    // Ejemplo: actividadDao.insert(ActividadEntity(id = i, ...))
                    android.util.Log.w("CounterVM", "¡Falta la Actividad $i en la DB! El progreso no se guardará.")
                }
            }
        }
    }
}