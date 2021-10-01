package br.com.prado.eduardo.luiz.domain.model

data class RepositoryModel(
  val id: Long,
  val name: String,
  val fullName: String,
  val isPrivate: Boolean,
  val owner: OwnerModel,
  val description: String?,
  val url: String,
  val forks: Long,
)
