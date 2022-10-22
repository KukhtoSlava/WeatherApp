package com.kukhtoslava.flowredux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

public sealed interface FlowReduxStore<Action, State> {
  public val coroutineScope: CoroutineScope

  public val stateFlow: StateFlow<State>

  /** Get streams of actions.
   *
   * This [Flow] includes dispatched [Action]s (via [dispatch] function)
   * and [Action]s returned from [SideEffect]s.
   */
  public val actionSharedFlow: SharedFlow<Action>

  /**
   * @return false if cannot dispatch action ([coroutineScope] was cancelled).
   */
  public fun dispatch(action: Action): Boolean
}

public fun <Action, State> createDefaultFlowReduxStore(
    coroutineScope: CoroutineScope,
    initialState: State,
    sideEffects: List<SideEffect<State, Action>>,
    reducer: Reducer<State, Action>
): FlowReduxStore<Action, State> = DefaultFlowReduxStore(
  coroutineScope = coroutineScope,
  initialState = initialState,
  sideEffects = sideEffects,
  reducer = reducer
)
