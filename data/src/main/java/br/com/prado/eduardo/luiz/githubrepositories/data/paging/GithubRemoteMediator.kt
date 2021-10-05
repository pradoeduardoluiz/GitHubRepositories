package br.com.prado.eduardo.luiz.githubrepositories.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RemoteKeysDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.AppPreferences
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.RepositoryDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
  private val language: String,
  private val gitHubService: GitHubService,
  private val database: Database,
  private val appPreferences: AppPreferences
) : RemoteMediator<Int, RepositoryDBO>() {

  override suspend fun initialize(): InitializeAction {
    return if (isCacheExpired()) {
      InitializeAction.SKIP_INITIAL_REFRESH
    } else {
      InitializeAction.LAUNCH_INITIAL_REFRESH
    }
  }

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, RepositoryDBO>
  ): MediatorResult {

    val page = when (loadType) {
      LoadType.REFRESH -> {
        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
        remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
      }
      LoadType.PREPEND -> {
        val remoteKeys = getRemoteKeyForFirstItem(state)
        val prevKey = remoteKeys?.prevKey
          ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        prevKey
      }
      LoadType.APPEND -> {
        val remoteKeys = getRemoteKeyForLastItem(state)
        val nextKey = remoteKeys?.nextKey
          ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        nextKey
      }
    }

    val query = LANGUAGE_FILTER + language

    try {
      val apiResponse = gitHubService.getRepositories(query, page, state.config.pageSize)

      val repos = apiResponse.repositories.map(::mapToDbo)
      val endOfPaginationReached = repos.isEmpty()

      database.withTransaction {
        if (loadType == LoadType.REFRESH) {
          appPreferences.lastUpdate = getCurrentTime()
          database.remoteKeysDao().deleteAll()
          database.repositoryDao().deleteAll()
        }

        val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = repos.map { RemoteKeysDBO(repoId = it.id, prevKey = prevKey, nextKey = nextKey) }

        database.remoteKeysDao().insert(keys)
        database.repositoryDao().insert(repos)
      }
      return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (exception: IOException) {
      return MediatorResult.Error(exception)
    } catch (exception: HttpException) {
      return MediatorResult.Error(exception)
    }
  }

  private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepositoryDBO>): RemoteKeysDBO? {
    return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
      database.remoteKeysDao().getById(repo.id)
    }
  }

  private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepositoryDBO>): RemoteKeysDBO? {
    return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { repo ->
      database.remoteKeysDao().getById(repo.id)
    }
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

  private fun isCacheExpired(): Boolean {
    val cacheTimeout = TimeUnit.HOURS.toMillis(REMOTE_CACHE_TIMEOUT)
    return appPreferences.lastUpdate > 0 &&
      getCurrentTime() - appPreferences.lastUpdate <= cacheTimeout
  }

  private fun getCurrentTime() = System.currentTimeMillis()

  private companion object {
    const val STARTING_PAGE_INDEX = 1
    const val LANGUAGE_FILTER = "language:"
    const val REMOTE_CACHE_TIMEOUT = 1L
  }

}
