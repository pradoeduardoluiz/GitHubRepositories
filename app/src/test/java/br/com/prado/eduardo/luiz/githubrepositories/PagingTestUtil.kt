package br.com.prado.eduardo.luiz.githubrepositories

import androidx.paging.CombinedLoadStates
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import kotlinx.coroutines.test.TestCoroutineDispatcher

suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
  val dcb = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) = Unit
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
  }
  val items = mutableListOf<T>()
  val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
    override suspend fun presentNewList(
      previousList: NullPaddedList<T>,
      newList: NullPaddedList<T>,
      newCombinedLoadStates: CombinedLoadStates,
      lastAccessedIndex: Int,
      onListPresentable: () -> Unit
    ): Int? {
      for (idx in 0 until newList.size)
        items.add(newList.getFromStorage(idx))
      onListPresentable.invoke()
      return null
    }
  }
  dif.collectFrom(this)
  return items
}
