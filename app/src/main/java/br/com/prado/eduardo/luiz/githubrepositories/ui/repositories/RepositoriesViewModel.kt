package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mappers.mapToState
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
  private val getRepositoriesUseCase: GetRepositoriesUseCase,
  private val navigator: Navigator,
  dispatchersProvider: DispatchersProvider,
  @RepositoriesStateQualifier initialState: RepositoriesState
) : StateViewModelImpl<RepositoriesState, RepositoriesIntention>(
  dispatchersProvider = dispatchersProvider,
  initialState = initialState
), RepositoriesContract.ViewModel {

  private var isInitialized: Boolean = false

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.Initialized -> initialized(intention.language)
      is RepositoriesIntention.Pop -> navigator.pop()
    }
  }

  private suspend fun initialized(language: String) {
    if (!isInitialized) {
      isInitialized = true
      search(language)
    }
  }

  private suspend fun search(language: String) {
    getRepositoriesUseCase(GetRepositoriesUseCase.Params(language))
      .cachedIn(viewModelScope)
      .collectLatest { pagingDataModel ->
        val pagingData = pagingDataModel.mapToState()
        updateState {
          copy(
            isLoading = false,
            pagingData = pagingData
          )
        }
      }
  }

}
