package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.ViewModelTest
import br.com.prado.eduardo.luiz.githubrepositories.collectDataForTest
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Test

class RepositoriesViewModelTest : ViewModelTest() {

  private val getRepositoriesUseCase = mockk<GetRepositoriesUseCase>()
  private val navigator = mockk<Navigator>()
  private lateinit var viewModel: RepositoriesContract.ViewModel

  override fun setupSubject(dispatchersProvider: DispatchersProvider) {
    viewModel = RepositoriesViewModel(
      getRepositoriesUseCase = getRepositoriesUseCase,
      navigator = navigator,
      dispatchersProvider = dispatchersProvider,
      initialState = RepositoriesState()
    )
    Assert.assertNotNull(viewModel)
  }

  @Test
  fun `should get paging repositories`() = runBlockingTest {
    val language = "kotlin"

    val expected = listOf(
      RepositoriesState.Item(
        id = 1,
        name = "name",
        fullName = "fullName",
        isPrivate = false,
        ownerName = "name",
        ownerImageUrl = "avatarUrl",
        description = "description",
        url = "url",
        forks = 10,
        stars = 1
      )
    )

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

    val pagingDataModel = PagingData.from(listOf(model))
    val pagingDataModelFlow = flowOf(pagingDataModel)

    coEvery {
      getRepositoriesUseCase(GetRepositoriesUseCase.Params(language = language))
    } returns pagingDataModelFlow

    viewModel.publish(RepositoriesIntention.Initialized(language = language))
    val actual = viewModel.state.value.pagingData.collectDataForTest()
    Assert.assertEquals(expected, actual)

    coVerify(exactly = 1) {
      getRepositoriesUseCase(GetRepositoriesUseCase.Params(language = language))
    }

    confirmVerified(navigator, getRepositoriesUseCase)
  }

  @Test
  fun `should pop screen`() = runBlockingTest {

    coEvery { navigator.pop() } just runs

    viewModel.publish(RepositoriesIntention.Pop)

    coVerify(exactly = 1) { navigator.pop() }

    confirmVerified(navigator)
  }

}
