package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import br.com.prado.eduardo.luiz.githubrepositories.data.paging.GithubRemoteMediator
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class GitHubRepositoryImpl(
  private val gitHubService: GitHubService,
  private val database: Database
) : GitHubRepository {

  override suspend fun getRepositories(
    language: String
  ): Flow<PagingData<RepositoryModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
      ),
      remoteMediator = GithubRemoteMediator(
        language,
        gitHubService,
        database
      ),
      pagingSourceFactory = { database.repositoryDao().getRepositories(language) }
    ).flow.map { pagingData ->
      pagingData.map(::mapToModel)
    }
  }

  private fun mapToModel(repository: RepositoryDBO) = RepositoryModel(
    id = repository.id,
    name = repository.name,
    fullName = repository.fullName,
    isPrivate = repository.isPrivate,
    owner = OwnerModel(
      name = repository.ownerName,
      avatarUrl = repository.ownerAvatarUrl
    ),
    description = repository.description,
    url = repository.url,
    forks = repository.forks,
    stars = repository.stars
  )

  companion object {
    const val PAGE_SIZE = 30
  }

}
