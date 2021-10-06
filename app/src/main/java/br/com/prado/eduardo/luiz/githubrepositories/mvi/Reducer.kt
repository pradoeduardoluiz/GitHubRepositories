package br.com.prado.eduardo.luiz.githubrepositories.mvi

import kotlinx.coroutines.flow.MutableStateFlow

interface Reducer<T> {

  val mutableState: MutableStateFlow<T>

  suspend fun updateState(handler: suspend T.() -> T)
}

class ReducerImpl<T>(initialState: T) : Reducer<T> {

  override val mutableState by lazy { MutableStateFlow(initialState) }

  override suspend fun updateState(handler: suspend T.() -> T) {
    mutableState.value = handler(mutableState.value)
  }
}
