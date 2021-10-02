package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.data.paging.GithubPagingSource
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(private val gitHubService: GitHubService) : GitHubRepository {

  override suspend fun getRepositories(
    language: String
  ): Flow<PagingData<RepositoryModel>> {
    return Pager(
      config = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        GithubPagingSource(
          gitHubService = gitHubService,
          language = language
        )
      }
    ).flow
  }

  companion object {
    const val PAGE_SIZE = 30
  }

}
