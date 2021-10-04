package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoriesFragmentBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.isShimmering
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.watch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
    error.retry.setOnClickListener { adapter.retry() }
  }

  private fun bindOutputs() {
    watch(viewModel.state) { state ->
      adapter.submitData(viewLifecycleOwner.lifecycle, state.pagingData)
    }
  }

  private fun setupRecyclerView() = with(binding) {
    repositories.adapter = adapter.withLoadStateHeaderAndFooter(
      header = RepositoriesLoadStateAdapter { adapter.retry() },
      footer = RepositoriesLoadStateAdapter { adapter.retry() }
    )
    adapter.addLoadStateListener { loadState ->
      repositories.isVisible = loadState.isNotLoading()
      binding.error.root.isVisible = loadState.isError()
    }

    lifecycleScope.launchWhenCreated {
      adapter.loadStateFlow.collectLatest { loadStates ->
        binding.shimmer.isShimmering = loadStates.isLoading()
      }
    }
  }

  private fun CombinedLoadStates.isLoading() =
    this.source.refresh is LoadState.Loading

  private fun CombinedLoadStates.isNotLoading() =
    this.source.refresh is LoadState.NotLoading

  private fun CombinedLoadStates.isError() =
    this.source.refresh is LoadState.Error

}
