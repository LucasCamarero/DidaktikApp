package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa el progreso de un usuario en una actividad.
 *
 * Esta entidad actúa como una tabla de relación entre personas (alumnos) y
 * actividades, almacenando el estado de finalización y la fecha asociada.
 *
 * @property progreso_id Identificador único del registro de progreso.
 * Se genera automáticamente.
 * @property actividad_fk Identificador de la actividad asociada.
 * @property persona_fk Identificador de la persona (alumno) asociada al progreso.
 * @property completada Indicador de si la actividad ha sido completada.
 * Valor esperado: 1 (sí) o 0 (no).
 * @property fecha_completado Fecha en la que se completó la actividad.
 * Puede ser nula si la actividad no ha sido completada.
 */
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