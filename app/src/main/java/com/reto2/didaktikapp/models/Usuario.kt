package com.reto2.didaktikapp.models

/**
 * Modelo de dominio que representa a un usuario.
 *
 * Extiende la clase [Persona] añadiendo información específica del usuario.
 *
 * @property id Identificador único del usuario.
 * @property username Nombre de usuario del usuario.
 * @property nombreCompletoDiploma Nombre completo que aparece en el diploma.
 */
data class Usuario(
    override val id: Int,
    override val username: String,
    val nombreCompletoDiploma: String
) : Persona(id, username)