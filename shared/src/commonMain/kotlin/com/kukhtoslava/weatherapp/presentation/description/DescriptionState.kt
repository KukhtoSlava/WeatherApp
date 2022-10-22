package com.kukhtoslava.weatherapp.presentation.description

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

data class DescriptionState(
    val placeName: String,
    val fullWeather: FullWeather?
) {
    companion object {

        fun initial(): DescriptionState = DescriptionState(
            placeName = "",
            fullWeather = null
        )
    }
}
