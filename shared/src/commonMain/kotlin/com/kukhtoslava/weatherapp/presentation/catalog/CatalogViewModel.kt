package com.kukhtoslava.weatherapp.presentation.catalog

import com.kukhtoslava.flowredux.createDefaultFlowReduxStore
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.di.catalogDI
import com.kukhtoslava.weatherapp.domain.usecases.GetCachedCitiesUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentWeatherUseCase
import com.kukhtoslava.weatherapp.domain.usecases.SaveCurrentPlaceUseCase
import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.flow.receiveAsFlow
import org.kodein.di.instance

class CatalogViewModel : ViewModel() {

    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase by catalogDI.instance()
    private val getCachedCitiesUseCase: GetCachedCitiesUseCase by catalogDI.instance()
    private val saveCurrentPlaceUseCase: SaveCurrentPlaceUseCase by catalogDI.instance()
    private val appErrorMapper: AppErrorMapper by catalogDI.instance()

    private val store = createDefaultFlowReduxStore(
        coroutineScope = viewModelScope,
        initialState = CatalogState.initial(),
        sideEffects = CatalogSideEffect(
            getCurrentWeatherUseCase = getCurrentWeatherUseCase,
            getCachedCitiesUseCase = getCachedCitiesUseCase,
            saveCurrentPlaceUseCase = saveCurrentPlaceUseCase,
            appErrorMapper = appErrorMapper
        ).sideEffects,
        reducer = { state, action -> action.reduce(state) }
    )
    private val eventChannel = store.actionSharedFlow
        .mapNotNull { it.toCatalogSingleEventOrNull() }
        .buffer(Channel.UNLIMITED)
        .produceIn(viewModelScope)

    fun dispatch(action: CatalogAction) = store.dispatch(action)
    val stateFlow: CStateFlow<CatalogState> get() = store.stateFlow.cStateFlow()
    val eventFlow: CFlow<CatalogEvent> get() = eventChannel.receiveAsFlow().cFlow()
}
