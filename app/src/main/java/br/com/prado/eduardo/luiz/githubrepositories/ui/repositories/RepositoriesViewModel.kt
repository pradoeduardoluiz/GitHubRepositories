package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandler
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandlerImpl
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.mvi.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
  private val getRepositoriesUseCase: GetRepositoriesUseCase,
  dispatchersProvider: DispatchersProvider,
  @RepositoriesStateQualifier initialState: RepositoriesState
) : StateViewModelImpl<RepositoriesState, RepositoriesIntention>(
  dispatchersProvider = dispatchersProvider,
  initialState = initialState
), RepositoriesContract.ViewModel,
  PagingHandler<RepositoryModel> by PagingHandlerImpl() {

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.SearchRepositories ->
        searchRepositories(language = intention.language, shouldReset = true)
    }
  }

  private suspend fun searchRepositories(language: String, shouldReset: Boolean) {
    updateState { copy(isLoading = false) }
    handlePaging(
      reset = shouldReset,
      request = {
        getRepositoriesUseCase(
          GetRepositoriesUseCase.Params(
            language = language,
            page = getCurrentPage(),
            perPage = getPageSize()
          )
        )
      },
      onSuccess = { repositories ->
        val items = repositories.map(::mapToState)
        updateState {
          copy(
            isLoading = false,
            isLastPage = isLastPage,
            nextPage = if (isFirstPage()) items.toEvent() else null
          )
        }
      },
      onError = {
        updateState {
          copy(
            isLoading = false,
          )
        }
      }
    )
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
