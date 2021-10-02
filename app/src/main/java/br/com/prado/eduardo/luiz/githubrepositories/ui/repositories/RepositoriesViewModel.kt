package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.Search -> search(intention.language)
      is RepositoriesIntention.Pop -> navigator.pop()
    }
  }

  private suspend fun search(language: String) {
    getRepositoriesUseCase(GetRepositoriesUseCase.Params(language))
      .cachedIn(viewModelScope)
      .collect { pagingDataModel ->
        val pagingData = pagingDataModel.map { repository ->
          mapToState(repository)
        }
        updateState {
          copy(
            isLoading = false,
            pagingData = pagingData
          )
        }
      }
  }

  private fun mapToState(repository: RepositoryModel): RepositoriesState.Item {
    return RepositoriesState.Item(
      id = repository.id,
      name = repository.name,
      fullName = repository.fullName,
      isPrivate = repository.isPrivate,
      ownerName = repository.owner.name,
      ownerImageUrl = repository.owner.avatarUrl,
      description = repository.description,
      url = repository.url,
      forks = repository.forks
    )
  }

}
