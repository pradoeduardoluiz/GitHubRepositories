package br.com.prado.eduardo.luiz.githubrepositories.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface StatelessViewModel<Intention> {
  fun publish(intention: Intention)
}

abstract class StatelessViewModelImpl<Intention>(
  private val dispatchersProvider: DispatchersProvider,
) : ViewModel(), StatelessViewModel<Intention> {

  private var lastIntention: Intention? = null

  private val publisher = MutableSharedFlow<Intention>()

  init {
    publisher.onEach { intention ->
      viewModelScope.launch(dispatchersProvider.io) { handleIntentions(intention) }
      this.lastIntention = intention
    }.shareIn(viewModelScope, SharingStarted.Eagerly)
  }

  final override fun publish(intention: Intention) {
    viewModelScope.launch(dispatchersProvider.io) { publisher.emit(intention) }
  }

  abstract suspend fun handleIntentions(intention: Intention)

}
