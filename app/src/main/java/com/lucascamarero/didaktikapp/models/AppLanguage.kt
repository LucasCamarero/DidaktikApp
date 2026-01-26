package com.lucascamarero.didaktikapp.models

/**
 * Enumeración que representa los idiomas soportados por la aplicación.
 *
 * Cada valor del enum define un idioma y su correspondiente *language tag*
 *
 * @property tag Código de idioma asociado, compatible con estándares de localización.
 */
enum class AppLanguage(val tag: String) {
    EUSKERA("eu"),
    CASTELLANO("es"),
    INGLES("en")
}