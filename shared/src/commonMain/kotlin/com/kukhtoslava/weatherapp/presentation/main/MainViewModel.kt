package com.kukhtoslava.weatherapp.presentation.main

import com.kukhtoslava.flowredux.createDefaultFlowReduxStore
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.di.mainDI
import com.kukhtoslava.weatherapp.di.searchDI
import com.kukhtoslava.weatherapp.domain.usecases.*
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

class MainViewModel : ViewModel() {

    private val getCityByPlaceIdUseCase: GetCityByPlaceIdUseCase by mainDI.instance()
    private val getFullWeatherUseCase: GetFullWeatherUseCase by mainDI.instance()
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase by mainDI.instance()
    private val saveInMemoryFullWeatherUseCase: SaveInMemoryFullWeatherUseCase by mainDI.instance()
    private val saveCityUseCase: SaveCityUseCase by mainDI.instance()
    private val appErrorMapper: AppErrorMapper by searchDI.instance()

    private val store = createDefaultFlowReduxStore(
        coroutineScope = viewModelScope,
        initialState = MainState.initial(),
        sideEffects = MainSideEffect(
            getCityByPlaceIdUseCase = getCityByPlaceIdUseCase,
            getFullWeatherUseCase = getFullWeatherUseCase,
            getCurrentPlaceUseCase = getCurrentPlaceUseCase,
            saveCityUseCase = saveCityUseCase,
            saveInMemoryFullWeatherUseCase = saveInMemoryFullWeatherUseCase,
            appErrorMapper = appErrorMapper
        ).sideEffects,
        reducer = { state, action -> action.reduce(state) }
    )
    private val eventChannel = store.actionSharedFlow
        .mapNotNull { it.toMainSingleEventOrNull() }
        .buffer(Channel.UNLIMITED)
        .produceIn(viewModelScope)

    fun dispatch(action: MainAction) = store.dispatch(action)
    val stateFlow: CStateFlow<MainState> get() = store.stateFlow.cStateFlow()
    val eventFlow: CFlow<MainEvent> get() = eventChannel.receiveAsFlow().cFlow()
}
