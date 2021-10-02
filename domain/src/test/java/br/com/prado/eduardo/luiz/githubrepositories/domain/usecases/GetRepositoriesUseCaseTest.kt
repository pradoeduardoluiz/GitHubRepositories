package br.com.prado.eduardo.luiz.githubrepositories.domain.usecases

import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.PageModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
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
  }

  @Test
  fun `should get repositories`() = runBlocking {
    val language = "kotlin"
    val page = 1
    val perPage = 1

    val params = GetRepositoriesUseCase.Params(
      language = language,
      page = page,
      perPage = perPage
    )

    val expected = PageModel(
      currentPage = 1,
      items = listOf(
        RepositoryModel(
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
          forks = 10
        )
      ),
      totalItems = 1,
      totalPages = 1
    )

    coEvery { gitHubRepository.getRepositories(language, page, perPage) } returns expected
    val actual = getRepositoriesUseCase(params = params)
    Assert.assertEquals(expected, actual)

    coVerify(exactly = 1) { gitHubRepository.getRepositories(language, page, perPage) }

    confirmVerified(gitHubRepository)
  }

}
