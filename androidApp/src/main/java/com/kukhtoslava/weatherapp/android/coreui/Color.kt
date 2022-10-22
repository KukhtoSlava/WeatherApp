package com.kukhtoslava.weatherapp.android.coreui

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val Rhino = Color(0xFF2E335A)
internal val PortGore = Color(0xFF1F1D47)
internal val Whisper = Color(0xFFEBEBF5)
internal val Whisper50 = Color(0x80EBEBF5)
internal val Whisper20 = Color(0x33EBEBF5)
internal val Gigas = Color(0xFF48319D)
internal val Gigas20 = Color(0x3348319D)


internal val mainBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF5936B4),
        Color(0xFF362A84)
    )
)

internal val WeatherColorScheme = lightColorScheme(
    primary = Rhino,
    onPrimary = Color.White,
    primaryContainer = Rhino,
    secondary = Rhino,
    onSecondary = Color.White,
    error = Color.Red,
    onError = Color.White
)
