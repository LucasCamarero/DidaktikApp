package com.lucascamarero.didaktikapp.data.repositories

import com.lucascamarero.didaktikapp.data.db.daos.ContenidoDao
import com.lucascamarero.didaktikapp.data.db.daos.ProgresoDao
import com.lucascamarero.didaktikapp.data.db.entities.ProgresoUsuarioEntity // Asegurar importación
import javax.inject.Inject

/**
 * Repositorio encargado de gestionar la lógica de negocio relacionada
 * con el progreso de los usuarios.
 *
 * Actúa como capa intermedia entre los DAOs y las capas superiores
 * (ViewModels / Use Cases), encapsulando el acceso a datos y la
 * coordinación entre distintas fuentes.
 */
class ProgresoRepository @Inject constructor(
    private val progresoDao: ProgresoDao,
    private val contenidoDao: ContenidoDao // Necesario para obtener actividad_ids
) {

    /**
     * Obtiene el estado completo de la ruta de progreso de un alumno.
     *
     * Esta información se utiliza normalmente para representar el avance
     * del usuario en el mapa o vista de ruta de actividades.
     *
     * @param personaId Identificador del alumno.
     * @return Flujo con el progreso completo de la ruta.
     */
    fun getRutaProgreso(personaId: Int) = progresoDao.getRutaProgresoCompleto(personaId)

    /**
     * Marca una actividad concreta como completada para un alumno.
     *
     * Esta operación delega en el DAO un proceso de tipo *upsert*,
     * asegurando que el registro de progreso exista antes de ser actualizado.
     *
     * @param actividadId Identificador de la actividad.
     * @param personaId Identificador del alumno.
     */
    suspend fun markActivityAsCompleted(actividadId: Int, personaId: Int) {
        val currentDate = System.currentTimeMillis().toString() // Usar un formato de fecha real
        progresoDao.upsertProgresoCompletado(actividadId, personaId, currentDate)
    }

    /**
     * Inicializa los registros de progreso de un alumno recién registrado.
     *
     * Se crean entradas de progreso para todas las actividades disponibles,
     * marcándolas inicialmente como no completadas. Esto garantiza que
     * las consultas con LEFT JOIN sobre la tabla de progreso funcionen
     * correctamente desde el primer momento.
     *
     * @param personaId Identificador del alumno.
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
}