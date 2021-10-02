package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.PageModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.OwnerDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.PageDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.RepositoryDTO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GitHubRepositoryImplTest {

  private val githubService = mockk<GitHubService>()
  private lateinit var repository: GitHubRepository

  @Before
  fun `set up`() {
    repository = GitHubRepositoryImpl(githubService)
  }

  @Test
  fun `should get repositories`() = runBlocking {
    val serviceResponse = PageDTO(
      totalCount = 1,
      repositories = listOf(
        RepositoryDTO(
          id = 1,
          name = "name",
          fullName = "fullName",
          isPrivate = false,
          owner = OwnerDTO(
            id = 1,
            name = "name",
            avatarUrl = "avatarUrl"
          ),
          description = "description",
          url = "url",
          forks = 10
        )
      )
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

    val language = "language:kotlin"
    val currentPage = 1
    val perPage = 10

    coEvery {
      githubService.getRepositories(
        language = language,
        page = currentPage,
        perPage = perPage
      )
    } returns serviceResponse

    val actual = repository.getRepositories(language, currentPage, perPage)
    Assert.assertEquals(expected, actual)

    coVerify(exactly = 1) {
      githubService.getRepositories(
        language = language,
        page = currentPage,
        perPage = perPage
      )
    }

    confirmVerified(githubService)
  }

}
