package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

import android.content.Context
import androidx.paging.PagingData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.prado.eduardo.luiz.githubrepositories.data.paging.GithubRemoteMediator
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GitHubRepositoryImplTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  private val gitHubService = mockk<GitHubService>()

  private lateinit var gitHubRepository: GitHubRepository
  private lateinit var mediator: GithubRemoteMediator

  private val language = "kotlin"

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val database = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()

    mediator = GithubRemoteMediator(
      language = "",
      gitHubService = gitHubService,
      database = database
    )

    gitHubRepository = GitHubRepositoryImpl(gitHubService, database)
  }

  @Test
  fun shouldGetRepositories() = runBlocking {

    val model = RepositoryModel(
      id = 1,
      name = "name",
      fullName = "fullName",
      isPrivate = false,
      owner = OwnerModel(
        name = "name",
        avatarUrl = "avatarUrl"
      ),
      description = "description",
      url = "url",
      forks = 10,
      stars = 1
    )

    val expected = PagingData.from(listOf(model))
    val expectedFlow = flowOf(expected)

    gitHubRepository.getRepositories(language = language).collect { actual ->
      Assert.assertEquals(expected, actual)
    }

    coVerify(exactly = 1) { gitHubRepository.getRepositories(language) }

    confirmVerified(gitHubRepository)
  }

}
