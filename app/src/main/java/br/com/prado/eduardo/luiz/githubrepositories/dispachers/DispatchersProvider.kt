package br.com.prado.eduardo.luiz.githubrepositories.dispachers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
  val ui: CoroutineDispatcher
  val io: CoroutineDispatcher
  val default: CoroutineDispatcher
}

class AppDispatchersProvider : DispatchersProvider {
  override val ui: CoroutineDispatcher = Dispatchers.Main
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val default: CoroutineDispatcher = Dispatchers.Default
}

