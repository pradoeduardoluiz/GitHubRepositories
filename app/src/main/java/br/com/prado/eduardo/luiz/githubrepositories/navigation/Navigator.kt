package br.com.prado.eduardo.luiz.githubrepositories.navigation

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

interface NavigatorBinder {
  fun bind(navController: NavController, lifecycleScope: LifecycleCoroutineScope)
  fun unbind()
}

interface NavigatorRouter {
  fun navigate(
    directions: NavDirections,
    navOptionsBuilder: NavOptions.Builder? = null,
    withDefaultAnimation: Boolean = true
  )

  fun pop()
  fun popTo(destinationId: Int, inclusive: Boolean)
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl @Inject constructor(
  private val dispatchersProvider: DispatchersProvider
) : Navigator {

  private var navController: NavController? = null
  private var lifecycleScope: LifecycleCoroutineScope? = null

  override fun bind(navController: NavController, lifecycleScope: LifecycleCoroutineScope) {
    Log.d(TAG, "bind: ")
    this.navController = navController
    this.lifecycleScope = lifecycleScope
  }

  override fun unbind() {
    Log.d(TAG, "unbind: ")
    this.navController = null
    this.lifecycleScope = null
  }

  override fun navigate(
    directions: NavDirections,
    navOptionsBuilder: NavOptions.Builder?,
    withDefaultAnimation: Boolean
  ) {
    val navOptions = navOptionsBuilder ?: NavOptions.Builder()
    lifecycleScope?.launch(dispatchersProvider.ui) {
      navController?.navigate(directions, navOptions.build())
    }
  }

  override fun pop() {
    lifecycleScope?.launch(dispatchersProvider.ui) { navController?.popBackStack() }
  }

  override fun popTo(destinationId: Int, inclusive: Boolean) {
    lifecycleScope?.launch(dispatchersProvider.ui) {
      navController?.popBackStack(
        destinationId,
        inclusive
      )
    }
  }

  private companion object {
    const val TAG = "NavigatorImpl"
  }
}
