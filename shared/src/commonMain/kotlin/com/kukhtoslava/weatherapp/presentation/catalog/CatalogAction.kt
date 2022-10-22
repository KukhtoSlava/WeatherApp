package com.kukhtoslava.weatherapp.presentation.catalog

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather

sealed interface CatalogAction {

    fun reduce(state: CatalogState): CatalogState

    data class ClickCity(val placeName: String, val placeId: String) : CatalogAction {
        override fun reduce(state: CatalogState) = state
    }

    object Close : CatalogAction {
        override fun reduce(state: CatalogState) = state
    }

    object Load : CatalogAction {
        override fun reduce(state: CatalogState): CatalogState {
            return state.copy(
                isLoading = true
            )
        }
    }
}

internal sealed interface SideEffectAction : CatalogAction {

    data class WeatherResult(
        val placesWeatherMap: Map<DBPlace, CurrentWeather> = mapOf(),
        val appError: AppError? = null
    ) : SideEffectAction {
        override fun reduce(state: CatalogState): CatalogState {
            return state.copy(
                placesWeatherMap = placesWeatherMap,
                isLoading = false,
                error = appError,
            )
        }
    }

    object CityChanged : SideEffectAction {
        override fun reduce(state: CatalogState) = state
    }
}

internal fun CatalogAction.toCatalogSingleEventOrNull(): CatalogEvent? =
    when (this) {
        CatalogAction.Close, SideEffectAction.CityChanged -> CatalogEvent.Close
        CatalogAction.Load -> null
        is CatalogAction.ClickCity -> null
        is SideEffectAction.WeatherResult -> null
    }
