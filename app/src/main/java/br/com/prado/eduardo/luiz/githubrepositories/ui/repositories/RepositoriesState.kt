package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

data class RepositoriesState(
  val isLoading: Boolean = false,
  val isLastPage: Boolean = false,
) {
  data class Repository(
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
