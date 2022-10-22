package com.kukhtoslava.weatherapp.presentation.catalog

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather

data class CatalogState(
    val placesWeatherMap: Map<DBPlace, CurrentWeather>,
    val isLoading: Boolean,
    val error: AppError?
) {
    companion object {

        fun initial(): CatalogState = CatalogState(
            placesWeatherMap = mapOf(),
            isLoading = false,
            error = null
        )
    }
}
