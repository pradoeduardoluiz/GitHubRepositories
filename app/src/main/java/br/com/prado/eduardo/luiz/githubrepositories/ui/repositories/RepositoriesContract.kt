package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import br.com.prado.eduardo.luiz.githubrepositories.mvi.StateViewModel

class RepositoriesContract {
  interface ViewModel : StateViewModel<RepositoriesState, RepositoriesIntention>
}
