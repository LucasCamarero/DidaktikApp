package com.reto2.didaktikapp.models

/**
 * Modelo de dominio que representa a un profesor.
 *
 * Extiende la clase [Persona] añadiendo información específica del profesor.
 *
 * @property id Identificador único del profesor.
 * @property username Nombre de usuario del profesor.
 * @property emailContacto Correo electrónico de contacto del profesor.
 */
data class Profesor(
    override val id: Int,
    override val username: String,
    val emailContacto: String
) : Persona(id, username)