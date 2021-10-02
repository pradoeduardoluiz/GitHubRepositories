package br.com.prado.eduardo.luiz.githubrepositories.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.prado.eduardo.luiz.githubrepositories.databinding.ActivityMainBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val binding by viewBinding(ActivityMainBinding::inflate)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
  }

}
