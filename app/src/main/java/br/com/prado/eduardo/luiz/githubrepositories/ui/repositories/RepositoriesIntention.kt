package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

sealed class RepositoriesIntention {
  data class Initialized(val language: String) : RepositoriesIntention()
  object Pop : RepositoriesIntention()
}
