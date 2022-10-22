package com.kukhtoslava.weatherapp.presentation.description

import com.kukhtoslava.flowredux.SideEffect
import com.kukhtoslava.weatherapp.domain.models.CurrentPlace
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentPlaceUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetFromMemoryFullWeatherUseCase
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

@Suppress("NOTHING_TO_INLINE")
internal class DescriptionSideEffect(
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val getFromMemoryFullWeatherUseCase: GetFromMemoryFullWeatherUseCase
) {

    inline val sideEffects
        get() = listOf(
            loadFromMemoryFullWeather()
        )

    private inline fun loadFromMemoryFullWeather() =
        SideEffect<DescriptionState, DescriptionAction> { actionFlow, _, _ ->
            actionFlow.filterIsInstance<DescriptionAction.Load>()
                .map {
                    getCurrentPlaceUseCase()
                }
                .map { currentPlace ->
                    currentPlace?.let {
                        handleMemoryCache(it)
                    } ?: SideEffectAction.WeatherEmpty
                }
        }

    private suspend fun handleMemoryCache(currentPlace: CurrentPlace): SideEffectAction {
        val currentWeather = getFromMemoryFullWeatherUseCase(currentPlace.placeId)
        return currentWeather?.let {
            SideEffectAction.WeatherResult(
                placeName = currentPlace.placeName,
                fullWeather = it
            )
        } ?: SideEffectAction.WeatherEmpty
    }
}
