package com.kukhtoslava.weatherapp.presentation.description

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

sealed interface DescriptionAction {

    fun reduce(state: DescriptionState): DescriptionState

    object Close : DescriptionAction {
        override fun reduce(state: DescriptionState) = state
    }

    object Load : DescriptionAction {
        override fun reduce(state: DescriptionState) = state
    }
}

internal sealed interface SideEffectAction : DescriptionAction {

    data class WeatherResult(
        val placeName: String,
        val fullWeather: FullWeather
    ) : SideEffectAction {
        override fun reduce(state: DescriptionState): DescriptionState {
            return state.copy(
                placeName = placeName,
                fullWeather = fullWeather
            )
        }
    }

    object WeatherEmpty : SideEffectAction {
        override fun reduce(state: DescriptionState) = state
    }
}

internal fun DescriptionAction.toDescriptionSingleEventOrNull(): DescriptionEvent? =
    when (this) {
        is SideEffectAction.WeatherResult -> null
        DescriptionAction.Load -> null
        DescriptionAction.Close -> DescriptionEvent.Close
        SideEffectAction.WeatherEmpty -> DescriptionEvent.Close
    }
