package com.team12.climaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.team12.climaapp.ui.MainScreen
import com.team12.climaapp.ui.theme.ClimaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClimaAppTheme {
                MainScreen()
            }
        }
    }
}
