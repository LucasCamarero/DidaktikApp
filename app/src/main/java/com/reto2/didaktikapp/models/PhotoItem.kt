package com.reto2.didaktikapp.models

/**
 * Modelo de datos que representa un elemento fotográfico dentro de un juego
 * o actividad basada en emparejamiento de imágenes.
 *
 * @property id Identificador único del elemento fotográfico.
 * @property pairId Identificador del par al que pertenece la imagen.
 * @property drawable Recurso drawable asociado a la imagen.
 */
data class PhotoItem(
    val id: Int,
    val pairId: Int,
    val drawable: Int
)