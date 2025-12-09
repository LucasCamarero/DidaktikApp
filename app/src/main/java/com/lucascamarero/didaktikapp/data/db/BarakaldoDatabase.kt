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

@Database(
    entities = [
        PersonaEntity::class, UsuarioEntity::class, ProfesorEntity::class,
        LugarEntity::class, ActividadEntity::class, ImagenEntity::class,
        ProgresoUsuarioEntity::class
    ],
    version = 1, // Asegúrate de tener la versión correcta
    exportSchema = false
)
abstract class BarakaldoDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDao
    abstract fun contenidoDao(): ContenidoDao
    abstract fun progresoDao(): ProgresoDao
    // Aquí puedes incluir el código para la instancia Singleton de la DB
}