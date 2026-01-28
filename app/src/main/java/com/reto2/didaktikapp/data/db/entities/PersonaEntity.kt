package com.reto2.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa a una persona registrada en la aplicación.
 *
 * Esta entidad se almacena en la tabla `persona` y es utilizada por Room como
 * modelo persistente. Puede representar distintos tipos de usuarios, como
 * alumnado o profesorado, en función del valor del campo [tipo_persona].
 *
 * @property persona_id Identificador único de la persona en la base de datos.
 * Se genera automáticamente.
 * @property username Nombre de usuario asociado a la persona.
 * Debe ser único dentro del sistema.
 * @property password_hash Hash de la contraseña del usuario.
 * Nunca se almacena la contraseña en texto plano.
 * @property tipo_persona Tipo de persona registrada.
 * Valores esperados: "Usuario" o "Profesor".
 */
@Entity(tableName = "persona")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)
    val persona_id: Int = 0,
    val username: String, // Único
    val password_hash: String,
    val tipo_persona: String // 'Usuario' o 'Profesor'
)