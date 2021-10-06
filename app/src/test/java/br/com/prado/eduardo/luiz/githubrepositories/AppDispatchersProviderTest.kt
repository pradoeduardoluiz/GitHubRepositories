package br.com.prado.eduardo.luiz.githubrepositories

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class AppDispatchersProviderTest : DispatchersProvider {
  val test: TestCoroutineDispatcher = TestCoroutineDispatcher()
  override val ui: CoroutineDispatcher = test
  override val io: CoroutineDispatcher = test
  override val default: CoroutineDispatcher = test
}
