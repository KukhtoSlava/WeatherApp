package com.kukhtoslava.weatherapp.android.coreui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WeatherTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = WeatherColorScheme,
        shapes = WeatherShapes,
        content = content
    )
}
