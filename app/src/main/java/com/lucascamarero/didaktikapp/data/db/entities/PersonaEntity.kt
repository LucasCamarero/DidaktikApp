package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)
    val persona_id: Int = 0,
    val username: String, // Único
    val password_hash: String,
    val tipo_persona: String // 'Usuario' o 'Profesor'
)