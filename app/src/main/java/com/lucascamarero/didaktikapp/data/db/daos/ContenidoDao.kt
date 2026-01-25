package com.lucascamarero.didaktikapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucascamarero.didaktikapp.data.db.entities.ActividadEntity
import com.lucascamarero.didaktikapp.data.db.entities.ImagenEntity
import com.lucascamarero.didaktikapp.data.db.entities.LugarEntity

/**
 * DAO encargado de las operaciones de acceso a datos relacionadas con el contenido
 * principal de la aplicación.
 *
 * Gestiona la persistencia y recuperación de lugares, actividades e imágenes,
 * actuando como punto central para el poblado inicial de datos y las consultas
 * necesarias para el funcionamiento de la aplicación.
 */
@Dao
interface ContenidoDao {

    // Operaciones de POBLADO DE DATOS (DML INSERT)

    /**
     * Inserta una lista de lugares en la base de datos.
     *
     * @param lugares Lista de entidades [LugarEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLugares(lugares: List<LugarEntity>)

    /**
     * Inserta una lista de actividades en la base de datos.
     *
     * @param actividades Lista de entidades [ActividadEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActividades(actividades: List<ActividadEntity>)

    /**
     * Inserta una lista de imágenes en la base de datos.
     *
     * @param imagenes Lista de entidades [ImagenEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagenes(imagenes: List<ImagenEntity>)

    // Operaciones de LECTURA (SELECT)

    /**
     * Recupera todos los lugares almacenados en la base de datos.
     *
     * @return Lista de entidades [LugarEntity].
     */
    @Query("SELECT * FROM lugar ORDER BY lugar_id ASC")
    suspend fun getAllLugares(): List<LugarEntity>

    /**
     * Recupera una actividad concreta a partir de su identificador.
     *
     * @param actividadId Identificador de la actividad.
     * @return La entidad [ActividadEntity] asociada o `null` si no existe.
     */
    @Query("SELECT * FROM actividad WHERE actividad_id = :actividadId")
    suspend fun getActividadById(actividadId: Int): ActividadEntity?

    /**
     * Recupera únicamente los identificadores de todas las actividades.
     *
     * Este método se utiliza para inicializar o gestionar el progreso
     * del usuario sin necesidad de cargar las actividades completas.
     *
     * @return Lista de identificadores de actividad.
     */
    @Query("SELECT actividad_id FROM actividad ORDER BY actividad_id ASC")
    suspend fun getAllActividadesIds(): List<Int>
}