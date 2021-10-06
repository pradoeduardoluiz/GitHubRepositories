package br.com.prado.eduardo.luiz.githubrepositories.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.connection.ConnectionStateMonitor
import br.com.prado.eduardo.luiz.githubrepositories.databinding.ActivityMainBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)

  @Inject
  lateinit var navigator: Navigator

  @Inject
  lateinit var connectionStateMonitor: ConnectionStateMonitor

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    watchNetwork()
    setupNavigation()
  }

  private fun setupNavigation() {
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.navHostFragment) as NavHostFragment
    navigator.bind(navHostFragment.navController, lifecycleScope)
  }

  private fun watchNetwork() {
    lifecycleScope.launch {
      connectionStateMonitor.watchNetwork().collect { isConnected ->
        binding.noConnection.isVisible = !isConnected
      }
    }
  }

}
