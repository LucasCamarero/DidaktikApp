package com.lucascamarero.didaktikapp.models

/**
 * Representa un punto geográfico dentro del mapa de la aplicación.
 *
 * Esta clase se utiliza para almacenar la información básica necesaria
 * para identificar y posicionar un punto en el mapa, como su ubicación
 * y nombre descriptivo.
 *
 * @property id Identificador único del punto.
 * @property lat Latitud del punto en coordenadas geográficas.
 * @property lng Longitud del punto en coordenadas geográficas.
 * @property name Nombre descriptivo del punto.
 */
data class MapPoint(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String
)