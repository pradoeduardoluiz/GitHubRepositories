package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoriesFragmentBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesFragment : Fragment(R.layout.repositories_fragment) {

  private val binding by viewBinding(RepositoriesFragmentBinding::bind)
  private val viewModel: RepositoriesContract.ViewModel by viewModels<RepositoriesViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindInputs()
    bindOutputs()
  }

  private fun bindInputs() {

  }

  private fun bindOutputs() {

  }

}
