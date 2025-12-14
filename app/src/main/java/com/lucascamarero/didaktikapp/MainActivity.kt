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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 1. INYECTAMOS LA BASE DE DATOS PARA PODER ESCRIBIR DATOS DE PRUEBA
    @Inject
    lateinit var database: BarakaldoDatabase
    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DidaktikAppTheme(dynamicColor = false) {
                Surface {
                    var showSplash by rememberSaveable { mutableStateOf(true) }
                    var showApp by rememberSaveable { mutableStateOf(false) }
                    val languageViewModel: LanguageViewModel = viewModel()
                    /*
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
                    }*/
                    ScreenManager(
                        languageViewModel = languageViewModel
                    )
                }
            }
        }
    }
}