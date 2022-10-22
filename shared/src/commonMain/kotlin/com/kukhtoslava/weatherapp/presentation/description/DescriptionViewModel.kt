package com.kukhtoslava.weatherapp.presentation.description

import com.kukhtoslava.flowredux.createDefaultFlowReduxStore
import com.kukhtoslava.weatherapp.di.descriptionDI
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentPlaceUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetFromMemoryFullWeatherUseCase
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

class DescriptionViewModel : ViewModel() {

    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase by descriptionDI.instance()
    private val getFromMemoryFullWeatherUseCase: GetFromMemoryFullWeatherUseCase by descriptionDI.instance()

    private val store = createDefaultFlowReduxStore(
        coroutineScope = viewModelScope,
        initialState = DescriptionState.initial(),
        sideEffects = DescriptionSideEffect(
            getCurrentPlaceUseCase = getCurrentPlaceUseCase,
            getFromMemoryFullWeatherUseCase = getFromMemoryFullWeatherUseCase
        ).sideEffects,
        reducer = { state, action -> action.reduce(state) }
    )
    private val eventChannel = store.actionSharedFlow
        .mapNotNull { it.toDescriptionSingleEventOrNull() }
        .buffer(Channel.UNLIMITED)
        .produceIn(viewModelScope)

    fun dispatch(action: DescriptionAction) = store.dispatch(action)
    val stateFlow: CStateFlow<DescriptionState> get() = store.stateFlow.cStateFlow()
    val eventFlow: CFlow<DescriptionEvent> get() = eventChannel.receiveAsFlow().cFlow()
}
