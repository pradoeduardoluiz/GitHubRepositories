package br.com.prado.eduardo.luiz.githubrepositories.ui.search

import br.com.prado.eduardo.luiz.githubrepositories.ViewModelTest
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.Assert
import org.junit.Test

class SearchViewModelTest : ViewModelTest() {

  private val navigator = mockk<Navigator>()
  private lateinit var viewModel: SearchContract.ViewModel

  override fun setupSubject(dispatchersProvider: DispatchersProvider) {
    viewModel = SearchViewModel(
      navigator = navigator,
      dispatchersProvider = dispatchersProvider,
      initialState = SearchState()
    )
    Assert.assertNotNull(viewModel)
  }

  @Test
  fun `should changed search text`() = runBlockingTest {
    val empty = ""
    viewModel.publish(SearchIntention.OnLanguageChanged(language = empty))
    Assert.assertEquals(false, viewModel.state.value.isSearchEnabled)

    val language = "kotlin"
    viewModel.publish(SearchIntention.OnLanguageChanged(language = language))
    Assert.assertEquals(true, viewModel.state.value.isSearchEnabled)
  }

  @Test
  fun `should navigate to repositories screen`() = runBlockingTest {
    val language = "kotlin"

    val directions = SearchFragmentDirections.searchToRepositories(language = language)

    coEvery { navigator.navigate(directions) } just runs

    viewModel.publish(SearchIntention.Search(language = language))

    coVerify(exactly = 1) {
      navigator.navigate(directions)
    }

    confirmVerified(navigator)
  }

}
