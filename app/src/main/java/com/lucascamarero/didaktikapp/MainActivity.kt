package com.lucascamarero.didaktikapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucascamarero.didaktikapp.ui.theme.DidaktikAppTheme
import com.lucascamarero.didaktikapp.screens.SplashScreen
import com.lucascamarero.didaktikapp.screens.IntroScreen
import com.lucascamarero.didaktikapp.viewmodels.LanguageViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DidaktikAppTheme(dynamicColor = false) {
                Surface {
                    var showSplash by rememberSaveable { mutableStateOf(true) }
                    var showApp by rememberSaveable { mutableStateOf(false) }
                    val languageViewModel: LanguageViewModel = viewModel()

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