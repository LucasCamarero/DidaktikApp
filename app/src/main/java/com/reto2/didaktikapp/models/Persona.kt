package com.reto2.didaktikapp.models

/**
 * Modelo base que representa a una persona dentro del dominio de la aplicación.
 *
 * Contiene los atributos comunes a los distintos tipos de personas y sirve
 * como clase padre para permitir herencia y polimorfismo en el modelo de dominio.
 *
 * @property id Identificador único de la persona.
 * @property username Nombre de usuario de la persona.
 */
open class Persona(
    open val id: Int,
    open val username: String
)