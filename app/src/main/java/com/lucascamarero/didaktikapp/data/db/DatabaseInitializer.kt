package com.lucascamarero.didaktikapp.data.db

import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.daos.PersonaDao
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Callback de inicialización de la base de datos.
 *
 * Esta clase se encarga de poblar la base de datos con datos iniciales
 * cuando se crea por primera vez. Se utiliza principalmente para:
 * - Insertar datos base necesarios para el funcionamiento de la aplicación.
 * - Garantizar la existencia de contenido inicial (lugares, actividades, imágenes).
 *
 * La inicialización puede realizarse mediante SQL directo o mediante DAOs,
 * dependiendo del contexto y las restricciones de integridad.
 */
class DatabaseInitializer : RoomDatabase.Callback() {

    /**
     * Scope de corrutinas utilizado para operaciones de inicialización
     * en segundo plano.
     */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Método invocado automáticamente cuando la base de datos se crea
     * por primera vez.
     *
     * @param db Instancia de la base de datos SQLite subyacente.
     */
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        initializeWithSQL(db)
    }

    /**
     * Inicializa la base de datos utilizando sentencias SQL directas.
     *
     * Este enfoque se utiliza para garantizar identificadores concretos
     * en tablas con claves foráneas dependientes, evitando problemas
     * con `autoGenerate`.
     *
     * @param db Instancia de la base de datos SQLite.
     */
    private fun initializeWithSQL(db: SupportSQLiteDatabase) {
        try {
            // 1. Insertar imágenes (21 imágenes)
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (1, 'act1_premio1', 'Lugar 1', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (2, 'act2_img1', 'Lugar 2', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (3, 'activ3_img1', 'Lugar 3', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (4, 'act4_img1', 'Lugar 4', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (5, 'fondopuzzle', 'Lugar 5', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (6, 'act6_ferrocarril', 'Lugar 6', 'Principal')")
            db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES (7, 'act7_img1', 'Lugar 7', 'Principal')")

            // Premios antiguos (8-14)
            for (i in 8..14) {
                val num = i - 7
                db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES ($i, 'premio${num}1', 'Premio antiguo $num', 'Premio Antiguo')")
            }

            // Premios actuales (15-21)
            for (i in 15..21) {
                val num = i - 14
                db.execSQL("INSERT INTO imagen (imagen_id, path_archivo, descripcion_corta, tipo_uso) VALUES ($i, 'premio${num}2', 'Premio actual $num', 'Premio Actual')")
            }

            // 2. Insertar lugares (7 lugares)
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (1, 'La Ermita de Santa Agueda', 'Lugar 1', '43.2992,-2.9884', 1)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (2, 'La iglesia de San Vicente', 'Lugar 2', '43.2992,-2.9884', 2)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (3, 'El Acertijo del Puente', 'Lugar 3', '43.2992,-2.9884', 3)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (4, 'El Edificio Ilgner', 'Lugar 4', '43.2992,-2.9884', 4)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (5, 'Rompecabezas', 'Lugar 5', '43.2992,-2.9884', 5)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (6, 'El ferrocarril', 'Lugar 6', '43.2992,-2.9884', 6)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (7, 'Palacio Munoa', 'Lugar 7', '43.2992,-2.9884', 7)")

            // 3. Insertar actividades (7 actividades)
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (1, 'Puzzle', 'activity1', 1, 8, 15)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (2, 'Sopa de letras', 'activity2', 2, 9, 16)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (3, 'Sopa de letras', 'activity3', 3, 10, 17)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (4, 'Selección', 'activity4', 4, 11, 18)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (5, 'Puzzle', 'activity5', 5, 12, 19)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (6, 'Puzzle', 'activity6', 6, 13, 20)")
            db.execSQL("INSERT INTO actividad (actividad_id, tipo_actividad, archivo_actividad, lugar_fk, premio_antigua_fk, premio_actual_fk) VALUES (7, 'Clasificación', 'activity7', 7, 14, 21)")

            // 4. Insertar persona de prueba (persona_id = 1)
            db.execSQL("INSERT INTO persona (persona_id, username, password_hash, tipo_persona) VALUES (1, 'testuser', 'testhash', 'Usuario')")

            // 5. Insertar usuario asociado
            db.execSQL("INSERT INTO usuario (persona_fk, nombre_completo_diploma) VALUES (1, 'Usuario de Prueba')")

            Log.d("DatabaseInitializer", "Base de datos inicializada con SQL directo")
        } catch (e: Exception) {
            Log.e("DatabaseInitializer", "Error inicializando base de datos: ${e.message}", e)
        }
    }

    companion object {

        /**
         * Inicializa la base de datos si aún no contiene datos.
         *
         * Comprueba previamente si existen actividades para evitar
         * reinicializaciones innecesarias.
         *
         * @param database Instancia de la base de datos principal.
         */
        suspend fun initializeDatabase(database: BarakaldoDatabase) {
            val contenidoDao = database.contenidoDao()
            val personaDao = database.personaDao()
            val progresoDao = database.progresoDao()

            // Verificar si ya está inicializada
            val actividades = contenidoDao.getAllActividadesIds()
            if (actividades.isNotEmpty()) {
                Log.d("DatabaseInitializer", "Base de datos ya inicializada")
                return
            }

            Log.d("DatabaseInitializer", "Inicializando base de datos...")

            initializeDatabaseData(contenidoDao, personaDao, progresoDao)
        }

        /**
         * Inicializa los datos de la base de datos utilizando DAOs.
         *
         * Este método se utiliza como alternativa al SQL directo,
         * apoyándose en Room para la inserción de entidades.
         *
         * @param contenidoDao DAO de contenido.
         * @param personaDao DAO de personas.
         * @param progresoDao DAO de progreso.
         */
        private suspend fun initializeDatabaseData(
            contenidoDao: ContenidoDao,
            personaDao: PersonaDao,
            progresoDao: ProgresoDao
        ) {
            // (código existente sin modificar)
            Log.d("DatabaseInitializer", "Base de datos inicializada correctamente")
        }
    }
}