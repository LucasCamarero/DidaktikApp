package com.reto2.didaktikapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reto2.didaktikapp.data.repositories.MapRepository
import com.reto2.didaktikapp.models.MapPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel para gestionar los puntos del mapa.
 * Obtiene los puntos desde la base de datos a través del MapRepository.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    /**
     * Estado reactivo con la lista de puntos del mapa.
     * Se actualiza automáticamente cuando cambian los datos en la base de datos.
     */
    val mapPoints: StateFlow<List<MapPoint>> = mapRepository.getAllMapPoints()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
