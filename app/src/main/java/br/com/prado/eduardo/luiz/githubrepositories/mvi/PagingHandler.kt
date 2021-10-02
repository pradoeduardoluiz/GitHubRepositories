package br.com.prado.eduardo.luiz.githubrepositories.mvi

import br.com.prado.eduardo.luiz.githubrepositories.domain.model.PageModel

interface PagingHandler<ResultType> {
  fun getCurrentPage(): Int
  fun getPageSize(): Int
  fun isFirstPage(): Boolean
  fun isLastPage(): Boolean
  fun getTotalItems(): Int

  suspend fun handlePaging(
    request: suspend () -> PageModel<ResultType>,
    onSuccess: suspend (List<ResultType>) -> Unit,
    onError: suspend (Throwable) -> Unit,
    reset: Boolean = false
  )
}

class PagingHandlerImpl<ResultType>(
  val defaultPageSize: Int = DEFAULT_PAGE_SIZE,
  val defaultFirstPage: Int = DEFAULT_FIRST_PAGE
) : PagingHandler<ResultType> {

  private var currentPage: Int = defaultFirstPage
  private var lastPage: Int = DEFAULT_LAST_PAGE
  private var totalItems: Int = 0

  private var allItems: MutableList<ResultType> = mutableListOf()

  override fun getCurrentPage(): Int = currentPage

  override fun getPageSize(): Int = if (isFirstPage()) defaultPageSize * 2 else defaultPageSize

  override fun isLastPage(): Boolean = lastPage == currentPage

  override fun isFirstPage(): Boolean = currentPage == defaultFirstPage

  override fun getTotalItems(): Int = totalItems

  override suspend fun handlePaging(
    request: suspend () -> PageModel<ResultType>,
    onSuccess: suspend (List<ResultType>) -> Unit,
    onError: suspend (Throwable) -> Unit,
    reset: Boolean
  ) {
    if (reset) resetPage()
    if (currentPage > lastPage) return

    try {
      val response = request()

      allItems.addAll(response.items)
      lastPage = response.totalPages
      totalItems = response.totalItems

      onSuccess(allItems)
      currentPage++
    } catch (exception: Exception) {
      currentPage = defaultFirstPage
      lastPage = DEFAULT_LAST_PAGE
      onError(exception)
    }
  }

  private fun resetPage() {
    currentPage = defaultFirstPage
    lastPage = DEFAULT_LAST_PAGE
    allItems = mutableListOf()
  }

  private companion object {
    const val DEFAULT_PAGE_SIZE = 10
    const val DEFAULT_FIRST_PAGE = 1
    const val DEFAULT_LAST_PAGE = Int.MAX_VALUE
  }

}
