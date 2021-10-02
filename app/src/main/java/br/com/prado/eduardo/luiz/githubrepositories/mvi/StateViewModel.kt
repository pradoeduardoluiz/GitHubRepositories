package br.com.prado.eduardo.luiz.githubrepositories.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface StateViewModel<State, Intention> {
  val state: StateFlow<State>
  fun publish(intention: Intention)
}

abstract class StateViewModelImpl<State, Intention>(
  private val dispatchersProvider: DispatchersProvider,
  initialState: State
) : ViewModel(), StateViewModel<State, Intention>, Reducer<State> by ReducerImpl(initialState) {

  private var lastIntention: Intention? = null

  private val publisher = MutableSharedFlow<Intention>()

  init {
    publisher.onEach { intention ->
      viewModelScope.launch(dispatchersProvider.io) { handleIntentions(intention) }
      this.lastIntention = intention
    }.shareIn(viewModelScope, SharingStarted.Eagerly)
  }

  final override val state: StateFlow<State> by lazy { mutableState }

  final override fun publish(intention: Intention) {
    viewModelScope.launch(dispatchersProvider.io) { publisher.emit(intention) }
  }

  abstract suspend fun handleIntentions(intention: Intention)

}

