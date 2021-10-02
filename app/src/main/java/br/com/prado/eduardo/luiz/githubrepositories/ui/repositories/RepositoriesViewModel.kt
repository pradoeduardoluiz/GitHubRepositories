package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandler
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandlerImpl
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
  private val getRepositoriesUseCase: GetRepositoriesUseCase,
  dispatchersProvider: DispatchersProvider,
  private val navigator: Navigator,
  @RepositoriesStateQualifier initialState: RepositoriesState
) : StateViewModelImpl<RepositoriesState, RepositoriesIntention>(
  dispatchersProvider = dispatchersProvider,
  initialState = initialState
), RepositoriesContract.ViewModel,
  PagingHandler<RepositoryModel> by PagingHandlerImpl() {

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.Search -> search(intention.language)
      is RepositoriesIntention.Pop -> navigator.pop()
    }
  }

  private suspend fun search(language: String): Flow<PagingData<RepositoryModel>> =
    getRepositoriesUseCase(GetRepositoriesUseCase.Params(language))
      .cachedIn(viewModelScope)
      .onEach { pagingDataModel ->
        val pagingData = pagingDataModel.map { model ->
          mapToState(model)
        }
        updateState { copy(pagingData = pagingData) }
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
