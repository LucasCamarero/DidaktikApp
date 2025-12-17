package com.lucascamarero.didaktikapp.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.lucascamarero.didaktikapp.models.AppLanguage

/**
 * ViewModel encargado de gestionar el idioma de la aplicación.
 *
 * Mantiene el idioma actual seleccionado por el usuario y se encarga
 * de aplicar el cambio de idioma a nivel de aplicación utilizando
 * `AppCompatDelegate`.
 */
class LanguageViewModel : ViewModel() {

    /**
     * Idioma actual de la aplicación.
     *
     * Se expone como estado observable de Compose y solo puede modificarse
     * internamente a través de [changeLanguage].
     */
    var currentLanguage by mutableStateOf(AppLanguage.EUSKERA)
        private set

    /**
     * Cambia el idioma de la aplicación.
     *
     * Actualiza el estado interno y aplica el nuevo locale
     * a toda la aplicación.
     *
     * @param language Idioma seleccionado por el usuario.
     */
    fun changeLanguage(language: AppLanguage) {
        currentLanguage = language

        val localeList = LocaleListCompat.forLanguageTags(language.tag)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
