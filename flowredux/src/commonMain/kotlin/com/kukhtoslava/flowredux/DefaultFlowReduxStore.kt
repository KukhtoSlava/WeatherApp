package com.kukhtoslava.flowredux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

internal class DefaultFlowReduxStore<Action, State>(
    override val coroutineScope: CoroutineScope,
    initialState: State,
    sideEffects: List<SideEffect<State, Action>>,
    reducer: Reducer<State, Action>
) : FlowReduxStore<Action, State> {
  private val _stateFlow = MutableStateFlow(initialState)
  private val _actionChannel = Channel<Action>(Channel.UNLIMITED)
  private val _actionSharedFlow = MutableSharedFlow<Action>(Channel.UNLIMITED)

  override val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

  init {
    val loopbacks = Array(sideEffects.size) { Channel<Action>() }

    val actionFlow = buildList(capacity = sideEffects.size + 1) {
      sideEffects.forEachIndexed { index, sideEffect ->
        add(
          sideEffect(
            loopbacks[index].consumeAsFlow(),
            stateFlow,
            coroutineScope,
          )
        )
      }
      add(_actionChannel.consumeAsFlow())
    }.merge()

    actionFlow
      .onEach { action ->
        _stateFlow.value = reducer(_stateFlow.value, action)

        loopbacks.sendAll(action)
        check(_actionSharedFlow.tryEmit(action)) { "Cannot send $action" }
      }
      .launchIn(coroutineScope)
  }

  override val actionSharedFlow: SharedFlow<Action> = _actionSharedFlow.asSharedFlow()
  override fun dispatch(action: Action): Boolean = _actionChannel
    .trySend(action)
    .isSuccess
}

private suspend fun <T> Array<Channel<T>>.sendAll(value: T) = coroutineScope {
  map { channel ->
    async { channel.send(value) }
  }.awaitAll()

  Unit
}
