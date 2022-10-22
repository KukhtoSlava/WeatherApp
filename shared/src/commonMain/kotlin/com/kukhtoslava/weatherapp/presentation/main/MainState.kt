package com.kukhtoslava.weatherapp.presentation.main

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

data class MainState(
    val placeName: String,
    val fullWeather: FullWeather?,
    val isLoading: Boolean,
    val error: AppError?,
) {
    companion object {

        fun initial(): MainState = MainState(
            placeName = "",
            fullWeather = null,
            isLoading = false,
            error = null
        )
    }
}
