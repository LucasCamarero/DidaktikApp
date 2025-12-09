package com.lucascamarero.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucascamarero.didaktikapp.data.db.entities.ActividadEntity
import com.lucascamarero.didaktikapp.data.db.entities.ImagenEntity
import com.lucascamarero.didaktikapp.data.db.entities.LugarEntity

@Dao
interface ContenidoDao {

    // --- Operaciones de PUEBLO DE DATOS (DML INSERT) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLugares(lugares: List<LugarEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActividades(actividades: List<ActividadEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagenes(imagenes: List<ImagenEntity>)

    // --- Operaciones de LECTURA (SELECT) ---

    /** Obtiene los 7 lugares de la ruta. */
    @Query("SELECT * FROM lugar ORDER BY lugar_id ASC")
    suspend fun getAllLugares(): List<LugarEntity>

    /** Obtiene los detalles de una actividad específica (para iniciar el juego). */
    @Query("SELECT * FROM actividad WHERE actividad_id = :actividadId")
    suspend fun getActividadById(actividadId: Int): ActividadEntity?

    // SOLUCIÓN: Método para obtener solo los IDs de actividad
    /** Obtiene solo los IDs de todas las actividades para inicializar el progreso. */
    @Query("SELECT actividad_id FROM actividad ORDER BY actividad_id ASC")
    suspend fun getAllActividadesIds(): List<Int>
}