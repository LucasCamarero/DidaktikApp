package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["persona_id"],
            childColumns = ["persona_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioEntity(
    @PrimaryKey // Clave Primaria y Foránea, fuerza la relación 1:1 con Persona
    val persona_fk: Int,
    val nombre_completo_diploma: String // Específico del alumno
)