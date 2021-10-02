package br.com.prado.eduardo.luiz.githubrepositories.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.viewBinding(bindingFactory: (View) -> T): ReadOnlyProperty<Fragment, T> =
  object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

    private var binding: T? = null

    init {
      this@viewBinding
        .viewLifecycleOwnerLiveData
        .observe(this@viewBinding, { owner ->
          owner?.lifecycle?.addObserver(this)
        })
    }

    override fun onDestroy(owner: LifecycleOwner) {
      binding = null
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
      return binding ?: bindingFactory(requireView()).also { newBinding ->
        binding = newBinding
      }
    }
  }

inline fun <reified T> Fragment.watch(source: StateFlow<T>, crossinline result: (T) -> Unit) {
  source.onEach { if (it != null) result(it) }.observeInLifecycle(this)
}

inline fun <reified T> Fragment.watch(source: SharedFlow<T>, crossinline result: (T) -> Unit) {
  source.onEach { if (it != null) result(it) }.observeInLifecycle(this)
}

