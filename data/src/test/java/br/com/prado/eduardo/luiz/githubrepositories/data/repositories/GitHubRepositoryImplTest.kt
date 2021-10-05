package br.com.prado.eduardo.luiz.githubrepositories.data.repositories

class GitHubRepositoryImplTest {

//  private val githubService = mockk<GitHubService>()
//  private val database = mockk<Database>()
//  private lateinit var gitHubRepository: GitHubRepository
//
//  @Before
//  fun `set up`() {
//    gitHubRepository = GitHubRepositoryImpl(
//      gitHubService = githubService,
//      database = database
//    )
//    Assert.assertNotNull(gitHubRepository)
//  }
//
//  @Test
//  fun `should get repositories`() = runBlocking {
//    val serviceResponse = PageDTO(
//      totalCount = 1,
//      repositories = listOf(
//        RepositoryDTO(
//          id = 1,
//          name = "name",
//          fullName = "fullName",
//          isPrivate = false,
//          owner = OwnerDTO(
//            name = "name",
//            avatarUrl = "avatarUrl"
//          ),
//          description = "description",
//          url = "url",
//          forks = 10
//        )
//      )
//    )
//
//    val expected = PageModel(
//      currentPage = 1,
//      items = listOf(
//        RepositoryModel(
//          id = 1,
//          name = "name",
//          fullName = "fullName",
//          isPrivate = false,
//          owner = OwnerModel(
//            name = "name",
//            avatarUrl = "avatarUrl"
//          ),
//          description = "description",
//          url = "url",
//          forks = 10
//        )
//      ),
//      totalItems = 1,
//      totalPages = 1
//    )
//
//    val language = "language:kotlin"
//    val currentPage = 1
//    val perPage = 10
//
//    coEvery {
//      githubService.getRepositories(
//        language = language,
//        page = currentPage,
//        perPage = perPage
//      )
//    } returns serviceResponse
//
//    val actual = gitHubRepository.getRepositories(language)
//    Assert.assertEquals(expected, actual)
//
//    coVerify(exactly = 1) {
//      githubService.getRepositories(
//        language = language,
//        page = currentPage,
//        perPage = perPage
//      )
//    }
//
//    confirmVerified(githubService)
//  }

}
