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

  override suspend fun initialize(): InitializeAction {
    // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
    // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
    // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
    // triggering remote refresh.
    return InitializeAction.LAUNCH_INITIAL_REFRESH
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
        // If remoteKeys is null, that means the refresh result is not in the database yet.
        // We can return Success with `endOfPaginationReached = false` because Paging
        // will call this method again if RemoteKeys becomes non-null.
        // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
        // the end of pagination for prepend.
        val prevKey = remoteKeys?.prevKey
        if (prevKey == null) {
          return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        }
        prevKey
      }
      LoadType.APPEND -> {
        val remoteKeys = getRemoteKeyForLastItem(state)
        // If remoteKeys is null, that means the refresh result is not in the database yet.
        // We can return Success with `endOfPaginationReached = false` because Paging
        // will call this method again if RemoteKeys becomes non-null.
        // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
        // the end of pagination for append.
        val nextKey = remoteKeys?.nextKey
        if (nextKey == null) {
          return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        }
        nextKey
      }
    }

    try {
      val apiResponse = gitHubService.getRepositories(language, page, state.config.pageSize)

      val repos = apiResponse.repositories.map(::mapToDbo)
      val endOfPaginationReached = repos.isEmpty()

      database.withTransaction {
        // clear all tables in the database
        if (loadType == LoadType.REFRESH) {
          database.remoteKeysDao().deleteAll()
          database.repositoryDao().deleteAll()
        }
        val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = repos.map {
          RemoteKeysDBO(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
        }
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
    // Get the last page that was retrieved, that contained items.
    // From that last page, get the last item
    return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
      ?.let { repo ->
        // Get the remote keys of the last item retrieved
        database.remoteKeysDao().getById(repo.id)
      }
  }

  private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepositoryDBO>): RemoteKeysDBO? {
    // Get the first page that was retrieved, that contained items.
    // From that first page, get the first item
    return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
      ?.let { repo ->
        // Get the remote keys of the first items retrieved
        database.remoteKeysDao().getById(repo.id)
      }
  }

  private suspend fun getRemoteKeyClosestToCurrentPosition(
    state: PagingState<Int, RepositoryDBO>
  ): RemoteKeysDBO? {
    // The paging library is trying to load data after the anchor position
    // Get the item closest to the anchor position
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
