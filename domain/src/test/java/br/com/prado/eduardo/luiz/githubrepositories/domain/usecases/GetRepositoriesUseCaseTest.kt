package br.com.prado.eduardo.luiz.githubrepositories.domain.usecases

import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetRepositoriesUseCaseTest {

  private val gitHubRepository = mockk<GitHubRepository>()
  private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

  @Before
  fun `set up`() {
    getRepositoriesUseCase = GetRepositoriesUseCase(gitHubRepository)
    Assert.assertNotNull(getRepositoriesUseCase)
  }

  @Test
  fun `should get repositories`() = runBlocking {
    val language = "kotlin"

    val params = GetRepositoriesUseCase.Params(language = language)

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

    coEvery { gitHubRepository.getRepositories(language) } returns expectedFlow
    getRepositoriesUseCase(params = params).collect { actual ->
      Assert.assertEquals(expected, actual)
    }

    coVerify(exactly = 1) { gitHubRepository.getRepositories(language) }

    confirmVerified(gitHubRepository)
  }

}
