package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoriesFragmentBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.addEndDrawable
import br.com.prado.eduardo.luiz.githubrepositories.extensions.onEndDrawableClicked
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.watch
import br.com.prado.eduardo.luiz.githubrepositories.mvi.handleEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoriesFragment : Fragment(R.layout.repositories_fragment) {

  private val binding by viewBinding(RepositoriesFragmentBinding::bind)
  private val viewModel: RepositoriesContract.ViewModel by viewModels<RepositoriesViewModel>()

  @Inject
  lateinit var adapter: RepositoriesAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    bindInputs()
    bindOutputs()
  }

  private fun bindInputs() = with(binding) {
    search.doAfterTextChanged { editable ->
      handleAfterTextChange(editable.toString())
    }
    search.onEndDrawableClicked {
      it.text = null
    }
  }

  private fun bindOutputs() {
    watch(viewModel.state) { state ->
      state.nextPage.handleEvent { items ->
        adapter.submitList(items)
      }
    }
  }

  private fun setupRecyclerView() {
    binding.repositories.setHasFixedSize(true)
    binding.repositories.adapter = adapter
  }

  private fun handleAfterTextChange(language: String) = with(binding) {
    search.addEndDrawable(if (language.isEmpty()) R.drawable.ic_search else R.drawable.ic_close)
    viewModel.publish(RepositoriesIntention.SearchRepositories(language = language))
  }

}
