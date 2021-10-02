package br.com.prado.eduardo.luiz.githubrepositories.domain.usecases

import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.shared.UseCase
import kotlinx.coroutines.flow.Flow

class GetRepositoriesUseCase(
  private val gitHubRepository: GitHubRepository
) : UseCase<GetRepositoriesUseCase.Params, Flow<PagingData<RepositoryModel>>> {

  override suspend fun invoke(params: Params): Flow<PagingData<RepositoryModel>> {
    return gitHubRepository.getRepositories(
      language = LANGUAGE_FILTER + params.language
    )
  }

  data class Params(
    val language: String
  )

  private companion object {
    const val LANGUAGE_FILTER = "language:"
  }

}
