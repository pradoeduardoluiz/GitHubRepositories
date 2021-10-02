package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
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
), RepositoriesContract.ViewModel {

  override suspend fun handleIntentions(intention: RepositoriesIntention) {
    when (intention) {
      is RepositoriesIntention.SearchRepositories -> Unit
    }
  }

}
