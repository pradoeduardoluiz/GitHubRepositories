package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

import br.com.prado.eduardo.luiz.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.domain.model.PageModel
import br.com.prado.eduardo.luiz.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.RepositoryDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import kotlin.math.ceil

class GitHubRepositoryImpl(private val gitHubService: GitHubService) : GitHubRepository {

  override suspend fun getRepositories(
    language: String,
    page: Int,
    perPage: Int
  ): PageModel<RepositoryModel> {
    val response = gitHubService.getRepositories(
      language = language,
      page = page,
      perPage = perPage
    )

    val totalPages = ceil(response.totalCount.toDouble() / perPage).toInt()
    return PageModel(
      currentPage = page,
      items = response.repositories.map(::mapToModel),
      totalItems = response.totalCount,
      totalPages = totalPages
    )
  }

  private fun mapToModel(repository: RepositoryDTO) = RepositoryModel(
    id = repository.id,
    name = repository.name,
    fullName = repository.fullName,
    isPrivate = repository.isPrivate,
    owner = OwnerModel(
      name = repository.owner.name,
      avatarUrl = repository.owner.avatarUrl
    ),
    description = repository.description,
    url = repository.url,
    forks = repository.forks
  )

}
