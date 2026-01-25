package com.lucascamarero.didaktikapp.data.db.mappers

import com.lucascamarero.didaktikapp.data.db.entities.PersonaEntity
import com.lucascamarero.didaktikapp.data.db.entities.UsuarioEntity
import com.lucascamarero.didaktikapp.models.Usuario

/**
 * Convierte una combinación de [PersonaEntity] y [UsuarioEntity] en un modelo de dominio [Usuario].
 *
 * Esta función de extensión se encarga de mapear los datos obtenidos desde la capa
 * de persistencia (Room) al modelo de dominio, combinando la información común de la persona
 * con los datos específicos del usuario.
 *
 * @param usuario Entidad que contiene la información específica del usuario.
 * @return Instancia de [Usuario] con los datos combinados de ambas entidades.
 */
fun PersonaEntity.toDomain(usuario: UsuarioEntity): Usuario =
    Usuario(
        id = persona_id,
        username = username,
        nombreCompletoDiploma = usuario.nombre_completo_diploma
    )