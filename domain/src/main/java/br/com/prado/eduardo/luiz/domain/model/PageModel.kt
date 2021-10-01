package br.com.prado.eduardo.luiz.domain.model

data class PageModel(
  val totalCount: Int,
  val repositories: List<RepositoryModel>
)
