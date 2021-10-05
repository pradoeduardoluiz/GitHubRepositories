package br.com.prado.eduardo.luiz.githubrepositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ViewModelTest : ViewModelTestMockk {

  @get:Rule
  val rule = InstantTaskExecutorRule()
  private val dispatchersProvider = AppDispatchersProviderTest()

  abstract fun setupSubject(dispatchersProvider: DispatchersProvider)

  override fun confirmAllVerified() = Unit

  @Before
  fun setupSubject() {
    setupSubject(dispatchersProvider)

    Dispatchers.setMain(dispatchersProvider.test)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  protected fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
    dispatchersProvider.test.runBlockingTest(block)
}

interface ViewModelTestMockk {
  fun confirmAllVerified()
}
