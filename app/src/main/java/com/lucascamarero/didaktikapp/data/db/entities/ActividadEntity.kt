package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val lugar_fk: Int, // FK a Lugar (Qué lugar tiene esta actividad)
    val premio_antigua_fk: Int, // FK a Imagen (La foto antigua de premio)
    val premio_actual_fk: Int // FK a Imagen (La foto actual de premio)
)