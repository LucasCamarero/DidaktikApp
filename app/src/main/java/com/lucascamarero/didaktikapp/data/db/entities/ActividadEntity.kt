package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa una actividad (juego) dentro de la aplicación.
 *
 * @property actividad_id Identificador único de la actividad.
 * Se genera automáticamente.
 * @property tipo_actividad Tipo de actividad o juego asociado
 * (por ejemplo, "Puzzle", "Sopa de letras").
 * @property archivo_actividad Identificador o ruta del archivo que define
 * la lógica o contenido de la actividad.
 * @property lugar_fk Identificador del lugar asociado a la actividad.
 * @property premio_antigua_fk Identificador de la imagen de premio antigua.
 * @property premio_actual_fk Identificador de la imagen de premio actual.
 */
@Entity(
    tableName = "actividad",
    foreignKeys = [
        ForeignKey(
            entity = LugarEntity::class,
            parentColumns = ["lugar_id"],
            childColumns = ["lugar_fk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ImagenEntity::class,
            parentColumns = ["imagen_id"],
            childColumns = ["premio_antigua_fk"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = ImagenEntity::class,
            parentColumns = ["imagen_id"],
            childColumns = ["premio_actual_fk"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val actividad_id: Int = 0,
    val tipo_actividad: String, // Ej: 'Puzzle', 'Sopa de letras'
    val archivo_actividad: String,
    val lugar_fk: Int,
    val premio_antigua_fk: Int,
    val premio_actual_fk: Int
)