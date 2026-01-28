package com.reto2.didaktikapp.di

import android.content.Context
import androidx.room.Room
import com.reto2.didaktikapp.data.db.BarakaldoDatabase
import com.reto2.didaktikapp.data.db.DatabaseInitializer
import com.reto2.didaktikapp.data.db.daos.ContenidoDao
import com.reto2.didaktikapp.data.db.daos.ProgresoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias de la aplicación.
 *
 * Este módulo define cómo Hilt debe proporcionar las dependencias
 * relacionadas con la base de datos y los DAOs, asegurando instancias
 * únicas a nivel de aplicación cuando sea necesario.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Proporciona la instancia principal de la base de datos de la aplicación.
     *
     * La base de datos se construye mediante Room y se configura como
     * singleton para garantizar una única instancia durante el ciclo
     * de vida de la aplicación.
     *
     * Además, se añade un callback de inicialización y se ejecuta
     * la carga inicial de datos en un scope de corrutinas.
     *
     * @param context Contexto de la aplicación proporcionado por Hilt.
     * @return Instancia de [BarakaldoDatabase].
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BarakaldoDatabase {

        /////////////////////////////////////////////
        // BORRAR LA BASE DE DATOS EN CADA ARRANQUE
        context.deleteDatabase("barakaldo_db")
        /////////////////////////////////////////////

        val database = Room.databaseBuilder(
            context,
            BarakaldoDatabase::class.java,
            "barakaldo_db"
        )
            .addCallback(DatabaseInitializer())
            .fallbackToDestructiveMigration() // Útil para desarrollo si cambias tablas
            .build()

        // Inicializar la base de datos en un coroutine scope
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        applicationScope.launch {
            DatabaseInitializer.initializeDatabase(database)
        }

        return database
    }

    /**
     * Proporciona el DAO encargado de gestionar el progreso de los usuarios.
     *
     * La instancia se obtiene directamente desde la base de datos.
     *
     * @param database Instancia de la base de datos.
     * @return Implementación de [ProgresoDao].
     */
    @Provides
    fun provideProgresoDao(database: BarakaldoDatabase): ProgresoDao {
        return database.progresoDao()
    }

    /**
     * Proporciona el DAO encargado del contenido de la aplicación.
     *
     * Incluye operaciones relacionadas con lugares, actividades
     * e imágenes.
     *
     * @param database Instancia de la base de datos.
     * @return Implementación de [ContenidoDao].
     */
    @Provides
    fun provideContenidoDao(database: BarakaldoDatabase): ContenidoDao {
        return database.contenidoDao() // Asume que tienes esta función en tu @Database
    }
}