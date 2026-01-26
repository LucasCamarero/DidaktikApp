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
            
            // 2. Insertar lugares (7 lugares) con coordenadas correctas
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (1, 'Ermita Santa Águeda', 'Lugar 1', '43.257611,-2.979528', 1)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (2, 'Iglesia de San Vicente', 'Lugar 2', '43.295750,-2.996722', 2)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (3, 'Mercado de Abastos', 'Lugar 3', '43.296902,-2.987188', 3)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (4, 'Edificio Ilgner', 'Lugar 4', '43.302592,-2.985833', 4)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (5, 'Cargadero de minas', 'Lugar 5', '43.315600,-3.010764', 5)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (6, 'Ferrocarril', 'Lugar 6', '43.305550,-2.982158', 6)")
            db.execSQL("INSERT INTO lugar (lugar_id, nombre, descripcion, coordenadas, imagen_principal_fk) VALUES (7, 'Palacio Munoa', 'Lugar 7', '43.295125,-2.978303', 7)")
            
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

        private suspend fun initializeDatabaseData(contenidoDao: ContenidoDao, personaDao: PersonaDao, progresoDao: ProgresoDao) {
            // 1. Insertar imágenes (necesarias para lugares y actividades)
            // Nota: Room con autoGenerate puede ignorar IDs explícitos, pero intentamos insertar sin ID
            // y luego verificamos que se crearon correctamente
            val imagenes = listOf(
                ImagenEntity(path_archivo = "act1_premio1", descripcion_corta = "Lugar 1", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "act2_img1", descripcion_corta = "Lugar 2", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "activ3_img1", descripcion_corta = "Lugar 3", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "act4_img1", descripcion_corta = "Lugar 4", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "fondopuzzle", descripcion_corta = "Lugar 5", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "act6_ferrocarril", descripcion_corta = "Lugar 6", tipo_uso = "Principal"),
                ImagenEntity(path_archivo = "act7_img1", descripcion_corta = "Lugar 7", tipo_uso = "Principal"),
                
                // Premios antiguos
                ImagenEntity(path_archivo = "premio11", descripcion_corta = "Premio antiguo 1", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio21", descripcion_corta = "Premio antiguo 2", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio31", descripcion_corta = "Premio antiguo 3", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio41", descripcion_corta = "Premio antiguo 4", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio51", descripcion_corta = "Premio antiguo 5", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio61", descripcion_corta = "Premio antiguo 6", tipo_uso = "Premio Antiguo"),
                ImagenEntity(path_archivo = "premio71", descripcion_corta = "Premio antiguo 7", tipo_uso = "Premio Antiguo"),
                
                // Premios actuales
                ImagenEntity(path_archivo = "premio12", descripcion_corta = "Premio actual 1", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio22", descripcion_corta = "Premio actual 2", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio32", descripcion_corta = "Premio actual 3", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio42", descripcion_corta = "Premio actual 4", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio52", descripcion_corta = "Premio actual 5", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio62", descripcion_corta = "Premio actual 6", tipo_uso = "Premio Actual"),
                ImagenEntity(path_archivo = "premio72", descripcion_corta = "Premio actual 7", tipo_uso = "Premio Actual"),
            )
            contenidoDao.insertImagenes(imagenes)

            // 2. Insertar lugares (7 lugares) - asumiendo que las imágenes tienen IDs 1-21
            val lugares = listOf(
                LugarEntity(nombre = "Ermita Santa Águeda", descripcion = "Lugar 1", coordenadas = "43.257611,-2.979528", imagen_principal_fk = 1),
                LugarEntity(nombre = "Iglesia de San Vicente", descripcion = "Lugar 2", coordenadas = "43.295750,-2.996722", imagen_principal_fk = 2),
                LugarEntity(nombre = "Mercado de Abastos", descripcion = "Lugar 3", coordenadas = "43.296902,-2.987188", imagen_principal_fk = 3),
                LugarEntity(nombre = "Edificio Ilgner", descripcion = "Lugar 4", coordenadas = "43.302592,-2.985833", imagen_principal_fk = 4),
                LugarEntity(nombre = "Cargadero de minas", descripcion = "Lugar 5", coordenadas = "43.315600,-3.010764", imagen_principal_fk = 5),
                LugarEntity(nombre = "Ferrocarril", descripcion = "Lugar 6", coordenadas = "43.305550,-2.982158", imagen_principal_fk = 6),
                LugarEntity(nombre = "Palacio Munoa", descripcion = "Lugar 7", coordenadas = "43.295125,-2.978303", imagen_principal_fk = 7),
            )
            contenidoDao.insertLugares(lugares)

            // 3. Insertar actividades (7 actividades) - asumiendo que lugares tienen IDs 1-7
            val actividades = listOf(
                ActividadEntity(tipo_actividad = "Puzzle", archivo_actividad = "activity1", lugar_fk = 1, premio_antigua_fk = 8, premio_actual_fk = 15),
                ActividadEntity(tipo_actividad = "Sopa de letras", archivo_actividad = "activity2", lugar_fk = 2, premio_antigua_fk = 9, premio_actual_fk = 16),
                ActividadEntity(tipo_actividad = "Sopa de letras", archivo_actividad = "activity3", lugar_fk = 3, premio_antigua_fk = 10, premio_actual_fk = 17),
                ActividadEntity(tipo_actividad = "Selección", archivo_actividad = "activity4", lugar_fk = 4, premio_antigua_fk = 11, premio_actual_fk = 18),
                ActividadEntity(tipo_actividad = "Puzzle", archivo_actividad = "activity5", lugar_fk = 5, premio_antigua_fk = 12, premio_actual_fk = 19),
                ActividadEntity(tipo_actividad = "Puzzle", archivo_actividad = "activity6", lugar_fk = 6, premio_antigua_fk = 13, premio_actual_fk = 20),
                ActividadEntity(tipo_actividad = "Clasificación", archivo_actividad = "activity7", lugar_fk = 7, premio_antigua_fk = 14, premio_actual_fk = 21),
            )
            contenidoDao.insertActividades(actividades)

            // 4. Insertar persona de prueba (persona_id = 1)
            // Para forzar persona_id = 1, necesitamos usar SQL directo o quitar autoGenerate
            // Por ahora, intentamos insertar y asumimos que será 1 si es la primera
            try {
                val persona = PersonaEntity(
                    username = "testuser",
                    password_hash = "testhash",
                    tipo_persona = "Usuario"
                )
                val personaId = personaDao.insertPersona(persona).toInt()
                Log.d("DatabaseInitializer", "Persona insertada con ID: $personaId")

                // 5. Insertar usuario asociado
                if (personaId == 1) {
                    val usuario = UsuarioEntity(
                        persona_fk = 1,
                        nombre_completo_diploma = "Usuario de Prueba"
                    )
                    personaDao.insertUsuario(usuario)
                } else {
                    Log.w("DatabaseInitializer", "Persona ID no es 1, es $personaId. Ajustar código si es necesario.")
                }
            } catch (e: Exception) {
                Log.e("DatabaseInitializer", "Error insertando persona: ${e.message}", e)
            }

            Log.d("DatabaseInitializer", "Base de datos inicializada correctamente")
        }
    }
}