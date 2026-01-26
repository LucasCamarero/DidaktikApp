package com.lucascamarero.didaktikapp.data.repositories

import android.util.Log
import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.entities.LugarEntity
import com.lucascamarero.didaktikapp.models.MapPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repositorio para gestionar los puntos del mapa.
 * Convierte las entidades de base de datos (LugarEntity) en modelos de dominio (MapPoint).
 */
class MapRepository @Inject constructor(
    private val contenidoDao: ContenidoDao
) {
    /**
     * Obtiene todos los puntos del mapa desde la base de datos.
     * Convierte LugarEntity a MapPoint parseando las coordenadas.
     */
    fun getAllMapPoints(): Flow<List<MapPoint>> {
        return contenidoDao.getAllLugaresFlow().map { lugares ->
            Log.d("MapRepository", "Obtenidos ${lugares.size} lugares de la BD")
            lugares.map { lugar ->
                val mapPoint = parseMapPoint(lugar)
                Log.d("MapRepository", "Parseado lugar: id=${mapPoint.id}, name=${mapPoint.name}, lat=${mapPoint.lat}, lng=${mapPoint.lng}")
                mapPoint
            }
        }
    }

    /**
     * Convierte una LugarEntity en un MapPoint.
     * Parsea las coordenadas del formato "lat,lng" a valores Double separados.
     */
    private fun parseMapPoint(lugar: LugarEntity): MapPoint {
        val coordenadas = lugar.coordenadas.split(",")
        val lat = coordenadas[0].trim().toDoubleOrNull() ?: 0.0
        val lng = coordenadas[1].trim().toDoubleOrNull() ?: 0.0
        
        if (lat == 0.0 || lng == 0.0) {
            Log.w("MapRepository", "Coordenadas inválidas para lugar ${lugar.lugar_id}: ${lugar.coordenadas}")
        }
        
        return MapPoint(
            id = lugar.lugar_id,
            lat = lat,
            lng = lng,
            name = lugar.nombre
        )
    }
}
