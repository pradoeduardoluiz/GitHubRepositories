package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.paging.PagingData

data class RepositoriesState(
  val isLoading: Boolean = true,
  val pagingData: PagingData<Item> = PagingData.empty(),
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
