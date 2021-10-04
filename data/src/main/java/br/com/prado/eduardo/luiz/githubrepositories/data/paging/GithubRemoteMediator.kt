package br.com.prado.eduardo.luiz.githubrepositories.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RemoteKeysDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.RepositoryDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
  private val language: String,
  private val gitHubService: GitHubService,
  private val database: Database,
) : RemoteMediator<Int, RepositoryDBO>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, RepositoryDBO>
  ): MediatorResult {

    val page = when (loadType) {
      LoadType.APPEND -> {
        val remoteKeys = getRemoteKeyForLastItem(state)
        val nextKey = remoteKeys?.nextKey
          ?: return MediatorResult.Success(
            endOfPaginationReached = remoteKeys != null
          )
        nextKey
      }
      LoadType.PREPEND -> {
        val remoteKeys = getRemoteKeyForFirstItem(state)
        val prevKey = remoteKeys?.prevKey
          ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        prevKey
      }
      LoadType.REFRESH -> {
        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
        remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
      }
    }

    return try {
      val response = gitHubService.getRepositories(
        language = language,
        page = page,
        perPage = state.config.pageSize
      )

      val repositories = response.repositories.map(::mapToDbo)
      val endOfPaginationReached = response.repositories.isEmpty()

      database.withTransaction {
        if (loadType == LoadType.REFRESH) {
          database.repositoryDao().deleteAll()
          database.remoteKeysDao().deleteAll()
        }

        val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = repositories.map {
          RemoteKeysDBO(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
        }
        database.remoteKeysDao().insert(keys)
        database.repositoryDao().insert(repositories)
      }
      MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (exception: IOException) {
      return MediatorResult.Error(exception)
    } catch (exception: HttpException) {
      return MediatorResult.Error(exception)
    }
  }

  private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepositoryDBO>): RemoteKeysDBO? {
    return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
      ?.let { repo -> database.remoteKeysDao().getById(repo.id) }
  }

  private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepositoryDBO>): RemoteKeysDBO? {
    return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
      ?.let { repo -> database.remoteKeysDao().getById(repo.id) }
  }

  private suspend fun getRemoteKeyClosestToCurrentPosition(
    state: PagingState<Int, RepositoryDBO>
  ): RemoteKeysDBO? {
    return state.anchorPosition?.let { position ->
      state.closestItemToPosition(position)?.id?.let { repoId ->
        database.remoteKeysDao().getById(repoId)
      }
    }
  }

  private fun mapToDbo(repository: RepositoryDTO) = RepositoryDBO(
    id = repository.id,
    name = repository.name,
    fullName = repository.fullName,
    isPrivate = repository.isPrivate,
    ownerName = repository.owner.name,
    ownerAvatarUrl = repository.owner.avatarUrl,
    description = repository.description,
    url = repository.url,
    forks = repository.forks,
    stars = repository.stars,
    language = repository.language
  )

  private companion object {
    const val STARTING_PAGE_INDEX = 1
  }

}
