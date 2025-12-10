package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progreso_usuario",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["actividad_id"],
            childColumns = ["actividad_fk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["persona_id"],
            childColumns = ["persona_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProgresoUsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val progreso_id: Int = 0,
    val actividad_fk: Int,
    val persona_fk: Int, // Referencia al alumno (Persona) que completa la actividad
    val completada: Int, // 1 para sí, 0 para no
    val fecha_completado: String?
)