package com.kukhtoslava.weatherapp.presentation.main

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

sealed interface MainAction {

    fun reduce(state: MainState): MainState

    object AddCityClicked : MainAction {
        override fun reduce(state: MainState) = state
    }

    object DescriptionClicked : MainAction {
        override fun reduce(state: MainState) = state
    }

    object CatalogClicked : MainAction {
        override fun reduce(state: MainState) = state
    }

    object LoadData : MainAction {
        override fun reduce(state: MainState): MainState {
            return state.copy(
                isLoading = true
            )
        }
    }
}

internal sealed interface SideEffectAction : MainAction {

    data class FullWeatherResult(
        val placeName: String = "",
        val fullWeather: FullWeather? = null,
        val appError: AppError? = null
    ) : SideEffectAction {
        override fun reduce(state: MainState): MainState {
            return state.copy(
                placeName = placeName,
                fullWeather = fullWeather,
                isLoading = false,
                error = appError
            )
        }
    }
}

internal fun MainAction.toMainSingleEventOrNull(): MainEvent? =
    when (this) {
        MainAction.AddCityClicked -> MainEvent.NavigationToSearch
        MainAction.CatalogClicked -> MainEvent.NavigationToCatalog
        MainAction.DescriptionClicked -> MainEvent.NavigationToDescription
        MainAction.LoadData -> null
        is SideEffectAction.FullWeatherResult -> {
            if (this.placeName.isEmpty()) {
                MainEvent.NavigationToSearch
            } else {
                null
            }
        }
    }
