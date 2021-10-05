package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mappers.RepositoriesMapper
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
  private val getRepositoriesUseCase: GetRepositoriesUseCase,
  private val navigator: Navigator,
  private val mapper: RepositoriesMapper,
  dispatchersProvider: DispatchersProvider,
  @RepositoriesStateQualifier initialState: RepositoriesState
) : StateViewModelImpl<RepositoriesState, RepositoriesIntention>(
  dispatchersProvider = dispatchersProvider,
  initialState = initialState
), RepositoriesContract.ViewModel {

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.Search -> search(intention.language)
      is RepositoriesIntention.Pop -> navigator.pop()
    }
  }

  private suspend fun search(language: String) {
    getRepositoriesUseCase(GetRepositoriesUseCase.Params(language))
      .collectLatest { pagingDataModel ->
        val pagingData = mapper.mapToState(pagingDataModel)
        updateState {
          copy(
            isLoading = false,
            pagingData = pagingData
          )
        }
      }
  }

}
