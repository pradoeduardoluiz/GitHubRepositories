package br.com.prado.eduardo.luiz.githubrepositories.ui.search

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModelImpl
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val navigator: Navigator,
  dispatchersProvider: DispatchersProvider,
  @SearchStateQualifier initialState: SearchState
) : StateViewModelImpl<SearchState, SearchIntention>(
  initialState = initialState,
  dispatchersProvider = dispatchersProvider
), SearchContract.ViewModel {

  override suspend fun handleIntentions(intention: SearchIntention) {
    when (intention) {
      is SearchIntention.Search -> openRepositories(intention.language)
      is SearchIntention.OnLanguageChanged -> checkEnableSearch(intention.language)
    }
  }

  private suspend fun checkEnableSearch(language: String) {
    updateState { copy(isSearchEnabled = language.isNotEmpty()) }
  }

  private fun openRepositories(language: String) {
    val directions = SearchFragmentDirections.searchToRepositories(language = language)
    navigator.navigate(directions)
  }

}
