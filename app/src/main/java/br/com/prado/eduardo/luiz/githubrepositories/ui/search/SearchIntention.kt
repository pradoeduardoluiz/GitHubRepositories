package br.com.prado.eduardo.luiz.githubrepositories.ui.search

sealed class SearchIntention {
  data class OnLanguageChanged(val language: String) : SearchIntention()
  data class Search(val language: String) : SearchIntention()
}
