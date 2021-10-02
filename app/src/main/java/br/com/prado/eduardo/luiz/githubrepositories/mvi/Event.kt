package br.com.prado.eduardo.luiz.githubrepositories.mvi

open class Event<T>(private val content: T) {

  private var hasBeenHandled = false

  val value: T?
    get() {
      return if (hasBeenHandled) {
        null
      } else {
        hasBeenHandled = true
        content
      }
    }

  class NoContent : Event<Unit>(Unit) {
    companion object Factory {
      fun create(): NoContent = NoContent()
    }
  }
}

fun <T> T.toEvent() = Event(this)

fun <T> Event<T>?.handleEvent(block: (T) -> Unit) {
  this?.value?.also(block)
}

fun Event.NoContent?.handleEvent(block: () -> Unit) {
  this?.value?.run { block() }
}
