package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa a un profesor dentro de la aplicación.
 *
 * Esta entidad está asociada de forma directa a [PersonaEntity] mediante una relación
 * uno a uno (1:1). Comparte el mismo identificador como clave primaria y clave foránea,
 * garantizando que cada usuario corresponde exactamente a una persona.
 *
 * @property persona_fk Identificador de la persona asociada.
 * Actúa como clave primaria y clave foránea.
 * @property email_contacto Correo electrónico de contacto del profesor.
 */
@Entity(
    tableName = "profesor",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["persona_id"],
            childColumns = ["persona_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProfesorEntity(
    @PrimaryKey
    val persona_fk: Int,
    val email_contacto: String
)