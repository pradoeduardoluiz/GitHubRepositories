package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

sealed class RepositoriesIntention {
  data class Search(val language: String) : RepositoriesIntention()
  object Pop : RepositoriesIntention()
}
