package com.reto2.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reto2.didaktikapp.data.db.entities.PersonaEntity
import com.reto2.didaktikapp.data.db.entities.ProfesorEntity
import com.reto2.didaktikapp.data.db.entities.UsuarioEntity

/**
 * DAO encargado de las operaciones de acceso a datos relacionadas con personas.
 *
 * Gestiona tanto la entidad base [PersonaEntity] como sus especializaciones
 * [UsuarioEntity] (alumno) y [ProfesorEntity], siguiendo un modelo de herencia
 * simulada mediante tablas relacionadas.
 *
 * Incluye operaciones de:
 * - Autenticación (login)
 * - Inserción de personas y sus subtipos
 * - Recuperación de datos específicos según el rol
 */
@Dao
interface PersonaDao {

    // Operaciones de LOGIN

    /**
     * Recupera una persona a partir de su nombre de usuario.
     *
     * Se utiliza principalmente durante el proceso de autenticación
     * para obtener el hash de la contraseña y el tipo de persona.
     *
     * @param username Nombre de usuario introducido en el login.
     * @return La entidad [PersonaEntity] asociada o `null` si no existe.
     */
    @Query("SELECT * FROM persona WHERE username = :username")
    suspend fun getPersonaByUsername(username: String): PersonaEntity?

    /**
     * Obtiene el nombre completo del alumno asociado a una persona.
     *
     * Esta consulta se utiliza para la generación de diplomas, accediendo
     * al campo específico almacenado en la tabla `usuario`.
     *
     * @param personaId Identificador de la persona.
     * @return Nombre completo para el diploma o `null` si no corresponde a un alumno.
     */
    @Query("""
        SELECT T2.nombre_completo_diploma
        FROM persona T1
        INNER JOIN usuario T2 ON T1.persona_id = T2.persona_fk
        WHERE T1.persona_id = :personaId
    """)
    suspend fun getNombreDiplomaByPersonaId(personaId: Int): String?

    // Operaciones de INSERT (Herencia)

    /**
     * Inserta una nueva persona en la tabla `persona`.
     *
     * Esta operación corresponde a la inserción de la superclase
     * dentro del modelo de herencia. El identificador devuelto
     * debe reutilizarse para insertar el subtipo correspondiente.
     *
     * @param persona Entidad base a insertar.
     * @return ID generado automáticamente para la persona.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPersona(persona: PersonaEntity): Long

    /**
     * Inserta un registro de tipo usuario (alumno).
     *
     * Debe existir previamente un registro en la tabla `persona`
     * con el mismo identificador.
     *
     * @param usuario Entidad [UsuarioEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    /**
     * Inserta un registro de tipo profesor.
     *
     * Debe existir previamente un registro en la tabla `persona`
     * con el mismo identificador.
     *
     * @param profesor Entidad [ProfesorEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfesor(profesor: ProfesorEntity)

    // Nota: El proceso de registro (insertar en Persona y luego en Usuario/Profesor)
    // debe ser gestionado por el Repositorio o un Use Case para asegurar la transacción.
}