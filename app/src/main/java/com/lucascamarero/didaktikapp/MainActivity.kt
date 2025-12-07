package com.lucascamarero.didaktikapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.lucascamarero.didaktikapp.ui.theme.DidaktikAppTheme
import com.lucascamarero.didaktikapp.screens.SplashScreen
import com.lucascamarero.didaktikapp.screens.IntroScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DidaktikAppTheme(dynamicColor = false) {
                Surface {
                    var showSplash by rememberSaveable { mutableStateOf(true) }
                    var showApp by rememberSaveable { mutableStateOf(false) }

                    when {
                        showSplash -> { SplashScreen(onTimeout = { showSplash = false }) }
                        !showApp -> { IntroScreen(onStartClick = { showApp = true }) }
                        else -> { ScreenManager() }
                    }
                }
            }
        }
    }
}

