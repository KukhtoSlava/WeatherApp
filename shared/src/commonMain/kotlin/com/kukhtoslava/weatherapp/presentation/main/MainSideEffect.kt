package com.kukhtoslava.weatherapp.presentation.main

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.takeUntil
import com.kukhtoslava.flowredux.SideEffect
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.usecases.*
import kotlinx.coroutines.flow.*

@Suppress("NOTHING_TO_INLINE")
internal class MainSideEffect(
    private val getCityByPlaceIdUseCase: GetCityByPlaceIdUseCase,
    private val getFullWeatherUseCase: GetFullWeatherUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val saveCityUseCase: SaveCityUseCase,
    private val saveInMemoryFullWeatherUseCase: SaveInMemoryFullWeatherUseCase,
    private val appErrorMapper: AppErrorMapper
) {

    inline val sideEffects
        get() = listOf(
            fullWeatherGet()
        )

    private inline fun fullWeatherGet() =
        SideEffect<MainState, MainAction> { actionFlow, _, coroutineScope ->
            val actionSharedFlow = actionFlow.shareIn(
                coroutineScope,
                SharingStarted.WhileSubscribed()
            )
            actionSharedFlow.filterIsInstance<MainAction.LoadData>()
                .map {
                    getCurrentPlaceUseCase()
                }
                .flatMapFirst { place ->
                    place?.let {
                        flowFromSuspend {
                            getCityByPlaceIdUseCase(place.placeId)
                        }.map { placeInfo ->
                            DBPlace(
                                name = placeInfo.result.formattedAddress,
                                placeId = placeInfo.result.placeId,
                                lat = placeInfo.result.geometry.location.lat,
                                lon = placeInfo.result.geometry.location.lng
                            )
                        }.flatMapConcat { dbPlace ->
                            flowFromSuspend {
                                getFullWeatherUseCase(
                                    lat = dbPlace.lat,
                                    lon = dbPlace.lon
                                )
                            }.onCompletion {
                                saveCityUseCase(
                                    dbPlace = dbPlace
                                )
                            }
                        }.flatMapConcat { fullWeather ->
                            flowFromSuspend {
                                saveInMemoryFullWeatherUseCase(
                                    placeId = place.placeId,
                                    fullWeather = fullWeather
                                )
                            }.map {
                                SideEffectAction.FullWeatherResult(
                                    placeName = place.placeName,
                                    fullWeather = fullWeather
                                )
                            }
                        }.catch { exception ->
                            emit(
                                SideEffectAction.FullWeatherResult(
                                    placeName = place.placeName,
                                    appError = appErrorMapper(exception)
                                )
                            )
                        }.takeUntil(
                            actionSharedFlow
                                .filter { action ->
                                    action == MainAction.CatalogClicked ||
                                            action == MainAction.AddCityClicked ||
                                            action == MainAction.DescriptionClicked
                                }
                        )
                    } ?: flowFromSuspend {
                        SideEffectAction.FullWeatherResult()
                    }
                }
        }
}
