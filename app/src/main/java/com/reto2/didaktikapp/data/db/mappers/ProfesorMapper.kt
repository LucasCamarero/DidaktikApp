package com.reto2.didaktikapp.data.db.mappers

import com.reto2.didaktikapp.data.db.entities.PersonaEntity
import com.reto2.didaktikapp.data.db.entities.ProfesorEntity
import com.reto2.didaktikapp.models.Profesor

/**
 * Convierte una combinación de [PersonaEntity] y [ProfesorEntity] en un modelo de dominio [Profesor].
 *
 * Esta función de extensión realiza el mapeo de los datos procedentes de la capa
 * de persistencia (Room) al modelo de dominio, combinando la información común de la persona
 * con los datos específicos del profesor.
 *
 * @param profesor Entidad que contiene la información específica del profesor.
 * @return Instancia de [Profesor] con los datos combinados de ambas entidades.
 */
fun PersonaEntity.toDomain(profesor: ProfesorEntity): Profesor =
    Profesor(
        id = persona_id,
        username = username,
        emailContacto = profesor.email_contacto
    )