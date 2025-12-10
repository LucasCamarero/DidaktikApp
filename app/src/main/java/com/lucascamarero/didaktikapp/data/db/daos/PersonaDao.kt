package com.lucascamarero.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucascamarero.didaktikapp.data.db.entities.PersonaEntity
import com.lucascamarero.didaktikapp.data.db.entities.ProfesorEntity
import com.lucascamarero.didaktikapp.data.db.entities.UsuarioEntity

@Dao
interface PersonaDao {

    // --- Operaciones de LOGIN ---

    /** Obtiene el hash y tipo de persona para autenticación. */
    @Query("SELECT * FROM persona WHERE username = :username")
    suspend fun getPersonaByUsername(username: String): PersonaEntity?

    /** Obtiene el nombre completo del alumno para el diploma. */
    @Query("""
        SELECT T2.nombre_completo_diploma
        FROM persona T1
        INNER JOIN usuario T2 ON T1.persona_id = T2.persona_fk
        WHERE T1.persona_id = :personaId
    """)
    suspend fun getNombreDiplomaByPersonaId(personaId: Int): String?

    // --- Operaciones de INSERT (Herencia) ---

    /** Inserta la superclase y retorna su ID (clave primaria). */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPersona(persona: PersonaEntity): Long

    /** Inserta el subtipo Usuario (Alumno). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    /** Inserta el subtipo Profesor. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfesor(profesor: ProfesorEntity)

    // Nota: El proceso de registro (insertar en Persona y luego en Usuario/Profesor)
    // debe ser gestionado por el Repositorio o un Use Case para asegurar la transacción.
}