package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imagen")
data class ImagenEntity(
    @PrimaryKey(autoGenerate = true)
    val imagen_id: Int = 0,
    val path_archivo: String,
    val descripcion_corta: String?, // Nullable
    val tipo_uso: String // 'Principal', 'Premio Antiguo', 'Premio Actual'
)