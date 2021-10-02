package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoriesFragmentBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.isShimmering
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.watch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RepositoriesFragment : Fragment(R.layout.repositories_fragment) {

  private val binding by viewBinding(RepositoriesFragmentBinding::bind)
  private val viewModel: RepositoriesContract.ViewModel by viewModels<RepositoriesViewModel>()
  private val args by navArgs<RepositoriesFragmentArgs>()

  @Inject
  lateinit var adapter: RepositoriesAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    bindInputs()
    bindOutputs()
    viewModel.publish(RepositoriesIntention.Search(language = args.language))
  }

  private fun bindInputs() = with(binding) {
    toolbar.setNavigationOnClickListener {
      viewModel.publish(RepositoriesIntention.Pop)
    }
  }

  private fun bindOutputs() {
    watch(viewModel.state) { state ->
      binding.shimmer.isShimmering = state.isLoading
      lifecycleScope.launch {
        adapter.submitData(state.pagingData)
      }
    }
  }

  private fun setupRecyclerView() {
    binding.repositories.setHasFixedSize(true)
    binding.repositories.adapter = adapter
  }

}
