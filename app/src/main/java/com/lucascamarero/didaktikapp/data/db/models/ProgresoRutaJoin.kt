package com.lucascamarero.didaktikapp.data.db.models
import androidx.room.ColumnInfo

/**
 * Clase DTO (Data Transfer Object) para mapear los resultados de la consulta JOIN
 * compleja en ProgresoDao.
 */
data class ProgresoRutaJoin(
    // Columnas de Lugar (T1)
    @ColumnInfo(name = "lugar_id")
    val lugarId: Int,
    @ColumnInfo(name = "lugar_nombre")
    val lugarNombre: String,

    // Columnas de Actividad (T2)
    @ColumnInfo(name = "actividad_id")
    val actividadId: Int,
    @ColumnInfo(name = "tipo_actividad")
    val tipoActividad: String,

    // Columnas de ProgresoUsuario (T3)
    @ColumnInfo(name = "completada")
    val completada: Int, // 1 si está completada, 0 si no

    // Columnas de Imagen (T4 y T5 - Premios)
    // Coinciden con los alias: img_antigua_path y img_actual_path
    @ColumnInfo(name = "img_antigua_path")
    val imgAntiguaPath: String,
    @ColumnInfo(name = "img_actual_path")
    val imgActualPath: String
)