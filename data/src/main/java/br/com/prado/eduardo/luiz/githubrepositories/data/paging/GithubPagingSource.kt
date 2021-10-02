package br.com.prado.eduardo.luiz.githubrepositories.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.prado.eduardo.luiz.githubrepositories.data.repositories.GitHubRepositoryImpl.Companion.PAGE_SIZE
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.RepositoryDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel

class GithubPagingSource(
  private val gitHubService: GitHubService,
  private val language: String
) : PagingSource<Int, RepositoryModel>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryModel> {
    return try {
      val page = params.key ?: STARTING_PAGE_INDEX
      val perPage = params.loadSize
      val response = gitHubService.getRepositories(
        language = language, page = page, perPage = perPage
      )
      val nextKey = if (response.repositories.isEmpty()) {
        null
      } else {
        page + (params.loadSize / PAGE_SIZE)
      }

      LoadResult.Page(
        data = response.repositories.map(::mapToModel),
        prevKey = null,
        nextKey = nextKey
      )
    } catch (e: Exception) {
      LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, RepositoryModel>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }
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

  private companion object {
    const val STARTING_PAGE_INDEX = 1
  }

}
