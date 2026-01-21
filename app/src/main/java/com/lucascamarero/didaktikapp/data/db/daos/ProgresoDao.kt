package com.lucascamarero.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lucascamarero.didaktikapp.data.db.entities.ProgresoUsuarioEntity
import com.lucascamarero.didaktikapp.data.db.models.ProgresoRutaJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgresoDao {


    @Query("""
    SELECT 
        T1.lugar_id, T1.nombre AS lugar_nombre, 
        T2.actividad_id, T2.tipo_actividad,
        T3.completada, 
        T4.path_archivo AS img_antigua_path, 
        T5.path_archivo AS img_actual_path
    FROM lugar T1 -- Asegurando minúsculas y nombres
    INNER JOIN actividad T2 ON T1.lugar_id = T2.lugar_fk
    LEFT JOIN progreso_usuario T3 ON T2.actividad_id = T3.actividad_fk AND T3.persona_fk = :personaId
    INNER JOIN imagen T4 ON T2.premio_antigua_fk = T4.imagen_id
    INNER JOIN imagen T5 ON T2.premio_actual_fk = T5.imagen_id
    ORDER BY T1.lugar_id ASC
""")
    fun getRutaProgresoCompleto(personaId: Int): Flow<List<ProgresoRutaJoin>>

    @Query("""
        UPDATE progreso_usuario 
        SET completada = 1, fecha_completado = :date
        WHERE actividad_fk = :actividadId AND persona_fk = :personaId
    """)
    suspend fun updateProgresoCompletado(actividadId: Int, personaId: Int, date: String)

    @Query("""
        SELECT COUNT(*) > 0 
        FROM progreso_usuario 
        WHERE actividad_fk = :actividadId AND persona_fk = :personaId
    """)
    suspend fun existsProgreso(actividadId: Int, personaId: Int): Boolean

    @Query("SELECT COUNT(*) > 0 FROM actividad WHERE actividad_id = :actividadId")
    suspend fun existsActividad(actividadId: Int): Boolean

    @Query("SELECT COUNT(*) > 0 FROM persona WHERE persona_id = :personaId")
    suspend fun existsPersona(personaId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingleProgreso(progreso: ProgresoUsuarioEntity)

    /**
     * Transacción que asegura que la fila existe antes de actualizarla.
     * Si la fila no existe, la crea primero con completada = 1.
     * Verifica que los foreign keys existan antes de intentar insertar.
     */
    @Transaction
    suspend fun upsertProgresoCompletado(actividadId: Int, personaId: Int, date: String) {
        android.util.Log.d("ProgresoDao", "upsertProgresoCompletado: actividadId=$actividadId, personaId=$personaId")
        
        // Verificamos que los foreign keys existan
        val actividadExists = existsActividad(actividadId)
        val personaExists = existsPersona(personaId)
        android.util.Log.d("ProgresoDao", "existsActividad($actividadId): $actividadExists, existsPersona($personaId): $personaExists")
        
        if (!actividadExists || !personaExists) {
            android.util.Log.e("ProgresoDao", "No se puede insertar: actividad o persona no existen. actividadExists=$actividadExists, personaExists=$personaExists")
            // No podemos insertar si los foreign keys no existen
            // Intentamos actualizar por si la fila ya existe (aunque es poco probable)
            updateProgresoCompletado(actividadId, personaId, date)
            return
        }
        
        // Primero verificamos si la fila existe
        val exists = existsProgreso(actividadId, personaId)
        android.util.Log.d("ProgresoDao", "existsProgreso: $exists")
        
        // Si no existe, intentamos crearla directamente con completada = 1
        if (!exists) {
            try {
                android.util.Log.d("ProgresoDao", "Intentando insertar nueva fila con completada=1...")
                insertSingleProgreso(
                    ProgresoUsuarioEntity(
                        actividad_fk = actividadId,
                        persona_fk = personaId,
                        completada = 1, // Insertamos directamente como completada
                        fecha_completado = date
                    )
                )
                android.util.Log.d("ProgresoDao", "Fila insertada correctamente con completada=1")
                // Si la inserción fue exitosa, ya está todo listo
                return
        } catch (e: Exception) {
                android.util.Log.e("ProgresoDao", "Error al insertar: ${e.message}", e)
                // Si falla por cualquier razón, intentamos actualizar
            }
        }
        
        // Si la fila existe o la inserción falló, intentamos actualizar
        android.util.Log.d("ProgresoDao", "Intentando actualizar fila existente...")
        updateProgresoCompletado(actividadId, personaId, date)
        android.util.Log.d("ProgresoDao", "Actualización completada")
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialProgreso(progreso: List<ProgresoUsuarioEntity>)

    @Query("SELECT COUNT(*) FROM progreso_usuario WHERE persona_fk = :personaId AND completada = 1")
    fun getCountCompletados(personaId: Int): Flow<Int>
}