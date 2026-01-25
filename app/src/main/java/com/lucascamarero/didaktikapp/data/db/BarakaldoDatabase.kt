package com.lucascamarero.didaktikapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.daos.PersonaDao
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import com.lucascamarero.didaktikapp.data.db.entities.ActividadEntity
import com.lucascamarero.didaktikapp.data.db.entities.ImagenEntity
import com.lucascamarero.didaktikapp.data.db.entities.LugarEntity
import com.lucascamarero.didaktikapp.data.db.entities.PersonaEntity
import com.lucascamarero.didaktikapp.data.db.entities.ProfesorEntity
import com.lucascamarero.didaktikapp.data.db.entities.ProgresoUsuarioEntity
import com.lucascamarero.didaktikapp.data.db.entities.UsuarioEntity

/**
 * Base de datos principal de la aplicación definida mediante Room.
 *
 * Esta clase actúa como punto de acceso central a la base de datos local,
 * declarando todas las entidades persistentes y los DAOs disponibles.
 *
 * La base de datos agrupa información relacionada con:
 * - Personas y sus roles (usuarios y profesores)
 * - Lugares y actividades
 * - Imágenes asociadas
 * - Progreso de los usuarios
 */
@Database(
    entities = [
        PersonaEntity::class, UsuarioEntity::class, ProfesorEntity::class,
        LugarEntity::class, ActividadEntity::class, ImagenEntity::class,
        ProgresoUsuarioEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BarakaldoDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de personas.
     *
     * @return Implementación de [PersonaDao].
     */
    abstract fun personaDao(): PersonaDao

    /**
     * Proporciona acceso al DAO de contenido.
     *
     * @return Implementación de [ContenidoDao].
     */
    abstract fun contenidoDao(): ContenidoDao

    /**
     * Proporciona acceso al DAO de progreso de usuarios.
     *
     * @return Implementación de [ProgresoDao].
     */
    abstract fun progresoDao(): ProgresoDao
}
