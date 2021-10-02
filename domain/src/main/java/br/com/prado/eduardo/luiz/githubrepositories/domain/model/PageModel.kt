package br.com.prado.eduardo.luiz.githubrepositories.domain.model

data class PageModel<T>(
  val currentPage: Int,
  val items: List<T>,
  val totalItems: Int,
  val totalPages: Int
)
