package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

sealed class RepositoriesIntention {
  data class SearchRepositories(val language: String) : RepositoriesIntention()
}
