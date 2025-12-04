package com.lucascamarero.didaktikapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
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
                    /*
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else {
                        IntroScreen()
                    }*/
                    ScreenManager()
                }
            }
        }
    }
}

