package com.reto2.didaktikapp.data.db.mappers
import androidx.room.ColumnInfo

/**
 * Clase DTO (Data Transfer Object) para mapear los resultados de la consulta JOIN
 * compleja en ProgresoDao.
 *
 * Este modelo no representa una tabla de base de datos, sino una proyección
 * de datos obtenida a partir de múltiples tablas relacionadas, utilizada
 * para mostrar el progreso del usuario a lo largo de una ruta de actividades.
 */
data class ProgresoRutaJoin(

    // Columnas de Lugar (T1)
    /**
     * Identificador único del lugar.
     */
    @ColumnInfo(name = "lugar_id")
    val lugarId: Int,

    /**
     * Nombre del lugar.
     */
    @ColumnInfo(name = "lugar_nombre")
    val lugarNombre: String,

    // Columnas de Actividad (T2)
    /**
     * Identificador único de la actividad asociada al lugar.
     */
    @ColumnInfo(name = "actividad_id")
    val actividadId: Int,

    /**
     * Tipo de actividad o juego.
     */
    @ColumnInfo(name = "tipo_actividad")
    val tipoActividad: String,

    // Columnas de ProgresoUsuario (T3)
    /**
     * Estado de finalización de la actividad por parte del usuario.
     *
     * Valores esperados:
     * - 1 → actividad completada
     * - 0 → actividad no completada
     */
    @ColumnInfo(name = "completada")
    val completada: Int, // 1 si está completada, 0 si no

    // Columnas de Imagen (T4 y T5 - Premios)
    /**
     * Ruta del archivo de la imagen de premio antigua asociada a la actividad.
     */
    // Coinciden con los alias: img_antigua_path y img_actual_path
    @ColumnInfo(name = "img_antigua_path")
    val imgAntiguaPath: String,

    /**
     * Ruta del archivo de la imagen de premio actual asociada a la actividad.
     */
    @ColumnInfo(name = "img_actual_path")
    val imgActualPath: String
)
