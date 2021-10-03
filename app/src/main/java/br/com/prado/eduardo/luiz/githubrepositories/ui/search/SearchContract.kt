package br.com.prado.eduardo.luiz.githubrepositories.ui.search

import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModel

interface SearchContract {
  interface ViewModel : StateViewModel<SearchState, SearchIntention>
}
