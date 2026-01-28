package com.reto2.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa una imagen almacenada o referenciada
 * por la aplicación.
 *
 * @property imagen_id Identificador único de la imagen en la base de datos.
 * Se genera automáticamente.
 * @property path_archivo Ruta del archivo de imagen en el almacenamiento
 * interno o externo.
 * @property descripcion_corta Descripción breve y opcional de la imagen
 * @property tipo_uso Tipo o categoría de uso de la imagen dentro de la aplicación.
 */
@Entity(tableName = "imagen")
data class ImagenEntity(
    @PrimaryKey(autoGenerate = true)
    val imagen_id: Int = 0,
    val path_archivo: String,
    val descripcion_corta: String?,
    val tipo_uso: String
)