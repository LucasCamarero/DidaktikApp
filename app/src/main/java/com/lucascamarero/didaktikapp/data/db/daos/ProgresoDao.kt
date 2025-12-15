package com.lucascamarero.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialProgreso(progreso: List<ProgresoUsuarioEntity>)

    @Query("SELECT COUNT(*) FROM progreso_usuario WHERE persona_fk = :personaId AND completada = 1")
    fun getCountCompletados(personaId: Int): Flow<Int>
}