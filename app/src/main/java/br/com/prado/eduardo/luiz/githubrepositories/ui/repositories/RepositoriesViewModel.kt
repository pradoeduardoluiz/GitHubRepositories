package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.lifecycle.viewModelScope
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandler
import br.com.prado.eduardo.luiz.githubrepositories.mvi.PagingHandlerImpl
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.mvi.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
  private val getRepositoriesUseCase: GetRepositoriesUseCase,
  private val dispatchersProvider: DispatchersProvider,
  @RepositoriesStateQualifier initialState: RepositoriesState
) : StateViewModelImpl<RepositoriesState, RepositoriesIntention>(
  dispatchersProvider = dispatchersProvider,
  initialState = initialState
), RepositoriesContract.ViewModel,
  PagingHandler<RepositoryModel> by PagingHandlerImpl() {

  private var searchQuery = MutableStateFlow("")

  init {
    watchForSearchQueryChanges()
  }

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.SearchRepositories -> setSearchQuery(intention.language)
    }
  }

  private suspend fun search(): Flow<List<RepositoryModel>> {
    return flow {
      updateState { copy(isLoading = false) }
      handlePaging(
        reset = false,
        request = {
          getRepositoriesUseCase(
            GetRepositoriesUseCase.Params(
              language = searchQuery.value,
              page = getCurrentPage(),
              perPage = getPageSize()
            )
          )
        },
        onSuccess = { repositories ->
          emit(repositories)
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
  }

  private fun watchForSearchQueryChanges() {
    viewModelScope.launch(dispatchersProvider.io) {
      searchQuery
        .debounce(TIMEOUT_MILLIS)
        .filter { query ->
          if (query.isEmpty()) {
            updateState {
              copy(firstPage = emptyList<RepositoriesState.Item>().toEvent())
            }
            return@filter false
          } else {
            return@filter true
          }
        }
        .flatMapLatest { search() }
        .flowOn(dispatchersProvider.io)
        .collect { repositories ->
          val items = repositories.map(::mapToState)

          val firstPageEvent = if (isFirstPage()) items.toEvent() else null
          val nextPageEvent = if (!isFirstPage()) items.toEvent() else null

          updateState {
            copy(
              isLoading = false,
              firstPage = firstPageEvent,
              nextPage = nextPageEvent,
            )
          }
        }
    }
  }

  private suspend fun setSearchQuery(query: String) {
    resetPage()
    if (query.isEmpty()) {
      updateState {
        copy(firstPage = emptyList<RepositoriesState.Item>().toEvent())
      }
    }
    searchQuery.value = query
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

  companion object {
    const val TIMEOUT_MILLIS = 300L
  }

}
