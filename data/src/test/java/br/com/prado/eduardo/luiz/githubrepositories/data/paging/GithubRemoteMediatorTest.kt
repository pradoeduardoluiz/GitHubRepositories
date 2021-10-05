package br.com.prado.eduardo.luiz.githubrepositories.data.paging

import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GithubRemoteMediatorTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  private var gitHubService = mockk<GitHubService>()
  private var database = mockk<Database>()
  private lateinit var githubRemoteMediator: GithubRemoteMediator

  @Before
  fun `set up`() {
    githubRemoteMediator = GithubRemoteMediator(
      gitHubService = gitHubService,
      database = database,
      language = "language"
    )
    Assert.assertNotNull(githubRemoteMediator)
  }

  @Test
  fun `should get repositories`() = runBlocking {

    val pagingState = PagingState<Int, RepositoryDBO>(
      listOf(),
      anchorPosition = null,
      PagingConfig(5),
      leadingPlaceholderCount = 5,
    )

    githubRemoteMediator.load(LoadType.REFRESH, pagingState)

    confirmVerified(
      gitHubService,
      database
    )
  }

}
