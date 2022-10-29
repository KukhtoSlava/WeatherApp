package com.kukhtoslava.weatherapp.presentation.search

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.takeUntil
import com.kukhtoslava.flowredux.SideEffect
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.usecases.*
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

private val REQUEST_DELAY = 600.milliseconds

@Suppress("NOTHING_TO_INLINE")
internal class SearchSideEffect(
    private val getPredictionsUseCase: GetPredictionsUseCase,
    private val saveCurrentPlaceUseCase: SaveCurrentPlaceUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val getCurrentDeviceLocationUseCase: GetCurrentDeviceLocationUseCase,
    private val getCityByLocationUseCase: GetCityByLocationUseCase,
    private val isLocationEnabledUseCase: IsLocationEnabledUseCase,
    private val permissionsWrapperController: PermissionsWrapperController,
    private val appErrorMapper: AppErrorMapper
) {

    inline val sideEffects
        get() = listOf(
            textChanged(),
            search(),
            cityChanged(),
            checkCurrentPlace(),
            checkCurrentLocation()
        )

    private inline fun textChanged() = SideEffect<SearchState, SearchAction> { actionFlow, _, _ ->
        actionFlow.filterIsInstance<SearchAction.Search>()
            .map { it.term.trim() }
            .debounce(REQUEST_DELAY)
            .distinctUntilChanged()
            .map { SideEffectAction.TextChanged(term = it) }
    }

    private inline fun search() =
        SideEffect<SearchState, SearchAction> { actionFlow, _, coroutineScope ->
            val actionSharedFlow = actionFlow.shareIn(
                coroutineScope,
                SharingStarted.WhileSubscribed()
            )

            actionSharedFlow.filterIsInstance<SideEffectAction.TextChanged>()
                .flatMapLatest<SideEffectAction.TextChanged, SearchAction> { action ->
                    executeGetPredictionsUseCase(term = action.term)
                }.takeUntil(
                    actionSharedFlow
                        .filterIsInstance<SearchAction.CloseScreen>()
                )
        }

    private inline fun cityChanged() = SideEffect<SearchState, SearchAction> { actionFlow, _, _ ->
        actionFlow.filterIsInstance<SearchAction.ClickCity>()
            .map {
                saveCurrentPlaceUseCase(
                    placeName = it.placeName,
                    placeId = it.placeId
                )
            }
            .distinctUntilChanged()
            .map { SideEffectAction.CityChanged }
    }

    private inline fun checkCurrentLocation() =
        SideEffect<SearchState, SearchAction> { actionFlow, _, coroutineScope ->
            val actionSharedFlow = actionFlow.shareIn(
                coroutineScope,
                SharingStarted.WhileSubscribed()
            )
            actionSharedFlow.filterIsInstance<SearchAction.ClickLocation>()
                .flatMapFirst {
                    flowFromSuspend {
                        try {
                            if (isLocationEnabledUseCase()) {
                                permissionsWrapperController.providePermission(Permission.COARSE_LOCATION)
                                val location = getCurrentDeviceLocationUseCase()
                                val city =
                                    getCityByLocationUseCase(lat = location.lat, lon = location.lng)
                                saveCurrentPlaceUseCase(
                                    placeName = city.results[0].name,
                                    placeId = city.results[0].placeId
                                )
                                SideEffectAction.LocationSuccess
                            } else {
                                SideEffectAction.LocationDisabled
                            }
                        } catch (deniedAlways: DeniedAlwaysException) {
                            SideEffectAction.LocationAlwaysDenied
                        } catch (denied: DeniedException) {
                            SideEffectAction.LocationDenied
                        } catch (denied: AppError.LocationException.LocationNotEnabledException) {
                            SideEffectAction.LocationDisabled
                        } catch (appError: AppError) {
                            SideEffectAction.LocationUnknownError(appError)
                        }
                    }
                }.takeUntil(
                    actionSharedFlow
                        .filterIsInstance<SearchAction.CloseScreen>()
                )
        }

    private inline fun checkCurrentPlace() =
        SideEffect<SearchState, SearchAction> { actionFlow, _, _ ->
            actionFlow.filterIsInstance<SearchAction.CheckCurrentPlace>()
                .flatMapFirst {
                    flowFromSuspend {
                        getCurrentPlaceUseCase()
                    }
                }.map { place ->
                    SideEffectAction.CurrentPlaceChecked(isExist = place != null)
                }
        }

    private fun executeGetPredictionsUseCase(
        term: String
    ): Flow<SideEffectAction> = flowFromSuspend {
        try {
            val predictions = if (term.isBlank()) {
                emptyList()
            } else {
                getPredictionsUseCase(query = term).predictions
            }
            SideEffectAction.SearchResult(
                predictions = predictions,
                term = term
            )
        } catch (ex: Exception) {
            SideEffectAction.SearchResult(
                appError = appErrorMapper(ex),
                term = term
            )
        }
    }
}
