package br.com.prado.eduardo.luiz.githubrepositories.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.ActivityMainBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)

  @Inject
  lateinit var navigator: Navigator

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    setupNavigation()
  }

  private fun setupNavigation() {
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.navHostFragment) as NavHostFragment
    navigator.bind(navHostFragment.navController, lifecycleScope)
  }

}
