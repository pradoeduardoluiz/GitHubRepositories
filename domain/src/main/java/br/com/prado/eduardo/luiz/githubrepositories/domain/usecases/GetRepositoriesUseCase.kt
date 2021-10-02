package br.com.prado.eduardo.luiz.githubrepositories.domain.usecases

import br.com.prado.eduardo.luiz.githubrepositories.domain.model.PageModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.shared.UseCase

class GetRepositoriesUseCase(
  private val gitHubRepository: GitHubRepository
) : UseCase<GetRepositoriesUseCase.Params, PageModel<RepositoryModel>> {

  override suspend fun invoke(params: Params): PageModel<RepositoryModel> {
    return gitHubRepository.getRepositories(
      language = params.language,
      page = params.page,
      perPage = params.perPage
    )
  }

  data class Params(
    val language: String,
    val page: Int,
    val perPage: Int
  )

}
