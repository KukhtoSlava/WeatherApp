package com.kukhtoslava.weatherapp.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kukhtoslava.weatherapp.android.coreui.WeatherTheme
import com.kukhtoslava.weatherapp.android.ui.MainContainer

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                MainContainer()
            }
        }
    }
}
