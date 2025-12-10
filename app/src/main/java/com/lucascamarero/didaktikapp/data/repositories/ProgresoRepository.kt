package com.lucascamarero.didaktikapp.data.repositories
import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import com.lucascamarero.didaktikapp.data.db.entities.ProgresoUsuarioEntity // Asegurar importación
import javax.inject.Inject


class ProgresoRepository @Inject constructor(
    private val progresoDao: ProgresoDao,
    private val contenidoDao: ContenidoDao // Necesario para obtener actividad_ids
) {

    /**
     * Obtiene el estado de la ruta del alumno para mostrarlo en el mapa (consulta compleja).
     */
    fun getRutaProgreso(personaId: Int) = progresoDao.getRutaProgresoCompleto(personaId)

    /**
     * DML UPDATE: Marca una actividad específica como completada.
     */
    suspend fun markActivityAsCompleted(actividadId: Int, personaId: Int) {
        val currentDate = System.currentTimeMillis().toString() // Usar un formato de fecha real
        progresoDao.updateProgresoCompletado(actividadId, personaId, currentDate)
    }

    /**
     * Inicializa los 7 registros de ProgresoUsuario para un alumno recién registrado.
     * Esto asegura que la tabla ProgresoUsuario tenga filas para el LEFT JOIN.
     */
    suspend fun initializeProgreso(personaId: Int) {
        // 1. Obtener todos los IDs de actividad disponibles (1 a 7)
        // ESTA LÍNEA AHORA FUNCIONA
        val allActivities = contenidoDao.getAllActividadesIds()

        // 2. Crear las 7 entidades de progreso iniciales
        val initialProgresoList = allActivities.map { actividadId ->
            // Kotlin ya puede inferir que actividadId es un Int
            ProgresoUsuarioEntity(
                actividad_fk = actividadId,
                persona_fk = personaId,
                completada = 0, // Pendiente
                fecha_completado = null
            )
        }

        // 3. Insertar en la BD
        progresoDao.insertInitialProgreso(initialProgresoList)
    }

    suspend fun updateProgresoCompletado(actividadId: Int, personaId: Int) {
        // lógica llamando al dao
    }
}