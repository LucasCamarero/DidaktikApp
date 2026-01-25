package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos que representa un lugar relevante dentro de la aplicación.
 *
 * Un lugar agrupa información descriptiva y geográfica, y está asociado a una imagen
 * principal que actúa como portada o referencia visual del mismo.
 *
 * @property lugar_id Identificador único del lugar en la base de datos.
 * Se genera automáticamente.
 * @property nombre Nombre del lugar.
 * @property descripcion Descripción textual del lugar.
 * @property coordenadas Coordenadas geográficas del lugar.
 * @property imagen_principal_fk Identificador de la imagen asociada como portada
 * del lugar.
 */
@Entity(
    tableName = "lugar",
    foreignKeys = [
        ForeignKey(
            entity = ImagenEntity::class,
            parentColumns = ["imagen_id"],
            childColumns = ["imagen_principal_fk"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class LugarEntity(
    @PrimaryKey(autoGenerate = true)
    val lugar_id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val coordenadas: String,
    val imagen_principal_fk: Int
)