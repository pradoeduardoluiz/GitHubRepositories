package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import br.com.prado.eduardo.luiz.githubrepositories.mvi.Event

data class RepositoriesState(
  val isShimmering: Boolean = true,
  val isLoading: Boolean = false,
  val isLastPage: Boolean = false,
  val firstPage: Event<List<Item>>? = null,
  val nextPage: Event<List<Item>>? = null,
) {
  data class Item(
    val id: Long,
    val name: String,
    val fullName: String,
    val isPrivate: Boolean,
    val ownerName: String,
    val ownerImageUrl: String,
    val description: String?,
    val url: String,
    val forks: Long,
  )
}
