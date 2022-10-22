package com.kukhtoslava.weatherapp.presentation.search

import com.kukhtoslava.flowredux.createDefaultFlowReduxStore
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.di.searchDI
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentPlaceUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetPredictionsUseCase
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

class SearchViewModel : ViewModel() {

    private val getPredictionsUseCase: GetPredictionsUseCase by searchDI.instance()
    private val saveCurrentPlaceUseCase: SaveCurrentPlaceUseCase by searchDI.instance()
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase by searchDI.instance()
    private val appErrorMapper: AppErrorMapper by searchDI.instance()

    private val store = createDefaultFlowReduxStore(
        coroutineScope = viewModelScope,
        initialState = SearchState.initial(),
        sideEffects = SearchSideEffect(
            getPredictionsUseCase = getPredictionsUseCase,
            saveCurrentPlaceUseCase = saveCurrentPlaceUseCase,
            getCurrentPlaceUseCase = getCurrentPlaceUseCase,
            appErrorMapper = appErrorMapper
        ).sideEffects,
        reducer = { state, action -> action.reduce(state) }
    )
    private val eventChannel = store.actionSharedFlow
        .mapNotNull { it.toSearchSingleEventOrNull() }
        .buffer(Channel.UNLIMITED)
        .produceIn(viewModelScope)

    fun dispatch(action: SearchAction) = store.dispatch(action)
    val stateFlow: CStateFlow<SearchState> get() = store.stateFlow.cStateFlow()
    val eventFlow: CFlow<SearchEvent> get() = eventChannel.receiveAsFlow().cFlow()
}
