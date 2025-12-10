package com.lucascamarero.didaktikapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val imagen_principal_fk: Int // FK a Imagen (La imagen de portada del lugar)
)