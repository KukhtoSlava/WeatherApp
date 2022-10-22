package com.kukhtoslava.weatherapp.presentation.catalog

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.kukhtoslava.flowredux.SideEffect
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather
import com.kukhtoslava.weatherapp.domain.usecases.GetCachedCitiesUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentWeatherUseCase
import com.kukhtoslava.weatherapp.domain.usecases.SaveCurrentPlaceUseCase
import kotlinx.coroutines.flow.*

@Suppress("NOTHING_TO_INLINE")
internal class CatalogSideEffect(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getCachedCitiesUseCase: GetCachedCitiesUseCase,
    private val saveCurrentPlaceUseCase: SaveCurrentPlaceUseCase,
    private val appErrorMapper: AppErrorMapper
) {

    inline val sideEffects
        get() = listOf(
            loadSavedPlaces(),
            cityChanged()
        )

    private inline fun loadSavedPlaces() =
        SideEffect<CatalogState, CatalogAction> { actionFlow, _, coroutineScope ->
            val actionSharedFlow = actionFlow.shareIn(
                coroutineScope,
                SharingStarted.WhileSubscribed()
            )

            actionSharedFlow.filterIsInstance<CatalogAction.Load>()
                .map {
                    getCachedCitiesUseCase()
                }
                .flatMapFirst { dbPlaces ->
                    val placesWeatherMap: MutableMap<DBPlace, CurrentWeather> =
                        mutableMapOf()
                    flowFromSuspend {
                        dbPlaces.forEach { dbPlace ->
                            val weather = getCurrentWeatherUseCase(
                                lat = dbPlace.lat,
                                lon = dbPlace.lon
                            )
                            placesWeatherMap[dbPlace] = weather
                        }
                        SideEffectAction.WeatherResult(
                            placesWeatherMap = placesWeatherMap
                        )
                    }.catch { exception ->
                        SideEffectAction.WeatherResult(
                            appError = appErrorMapper(exception)
                        )
                    }
                }
        }

    private inline fun cityChanged() = SideEffect<CatalogState, CatalogAction> { actionFlow, _, _ ->
        actionFlow.filterIsInstance<CatalogAction.ClickCity>()
            .map {
                saveCurrentPlaceUseCase(
                    placeName = it.placeName,
                    placeId = it.placeId
                )
            }
            .distinctUntilChanged()
            .map { SideEffectAction.CityChanged }
    }
}