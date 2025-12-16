package com.lucascamarero.didaktikapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucascamarero.didaktikapp.data.db.BarakaldoDatabase
import com.lucascamarero.didaktikapp.ui.theme.DidaktikAppTheme
import com.lucascamarero.didaktikapp.screens.SplashScreen
import com.lucascamarero.didaktikapp.screens.IntroScreen
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Actividad principal de la aplicación.
 *
 * Se encarga de:
 * - Inicializar dependencias mediante Hilt.
 * - Configurar el entorno visual con Jetpack Compose.
 * - Gestionar el flujo inicial de pantallas (Splash, Intro y app principal).
 * - Proveer el [LanguageViewModel] a las pantallas iniciales.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /**
     * Instancia de la base de datos inyectada mediante Hilt.
     *
     * Se utiliza principalmente para operaciones de persistencia
     * y pruebas de escritura de datos durante el ciclo de vida
     * de la actividad.
     */
    @Inject
    lateinit var database: BarakaldoDatabase

    /**
     * Metodo de inicialización de la actividad.
     *
     * Configura:
     * - El user agent requerido por la librería osmdroid.
     * - El renderizado edge-to-edge.
     * - El contenido Compose y el flujo de navegación inicial.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Configuración obligatoria del User Agent para osmdroid.
         * Evita errores al cargar recursos de mapas.
         */
        org.osmdroid.config.Configuration.getInstance().userAgentValue =
            packageName

        /**
         * Habilita el dibujo de la UI detrás de las barras del sistema.
         */
        enableEdgeToEdge()

        /**
         * Define la jerarquía de la interfaz usando Jetpack Compose.
         */
        setContent {
            DidaktikAppTheme(dynamicColor = false) {
                Surface {

                    /**
                     * Estado que controla la visibilidad del SplashScreen.
                     * Se conserva ante cambios de configuración.
                     */
                    var showSplash by rememberSaveable { mutableStateOf(true) }

                    /**
                     * Estado que indica si el usuario ha iniciado la aplicación.
                     */
                    var showApp by rememberSaveable { mutableStateOf(false) }

                    /**
                     * ViewModel compartido para la gestión del idioma.
                     */
                    val languageViewModel: LanguageViewModel = viewModel()

                    /**
                     * Control del flujo inicial de navegación basado en estados.
                     */

                    when {
                        showSplash -> {
                            SplashScreen(
                                onTimeout = { showSplash = false }
                            )
                        }
                        !showApp -> {
                            IntroScreen(
                                languageViewModel = languageViewModel,
                                onStartClick = { showApp = true }
                            )
                        }
                        else -> {
                            ScreenManager(
                                languageViewModel = languageViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}