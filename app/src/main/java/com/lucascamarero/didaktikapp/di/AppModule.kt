package com.lucascamarero.didaktikapp.di

import android.content.Context
import androidx.room.Room
import com.lucascamarero.didaktikapp.data.db.BarakaldoDatabase
import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. Enseñar a Hilt a crear la Base de Datos
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BarakaldoDatabase {
        return Room.databaseBuilder(
            context,
            BarakaldoDatabase::class.java,
            "barakaldo_db"
        )
            .fallbackToDestructiveMigration() // Útil para desarrollo si cambias tablas
            .build()
    }

    // 2. Enseñar a Hilt a crear el ProgresoDao (extrayéndolo de la DB)
    @Provides
    fun provideProgresoDao(database: BarakaldoDatabase): ProgresoDao {
        return database.progresoDao() // Asume que tienes esta función en tu BarakaldoDatabase
    }

    // Si tienes otros DAOs (PersonaDao, etc.), añádelos aquí igual que el de arriba.

    @Provides
    fun provideContenidoDao(database: BarakaldoDatabase): ContenidoDao {
        return database.contenidoDao() // Asume que tienes esta función en tu @Database
    }

}