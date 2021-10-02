package br.com.prado.eduardo.luiz.githubrepositories.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.SearchFragmentBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.viewBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.watch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

  private val binding by viewBinding(SearchFragmentBinding::bind)
  private val viewModel: SearchContract.ViewModel by viewModels<SearchViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindInputs()
    bindOutputs()
  }

  private fun bindInputs() = with(binding) {
    language.doOnTextChanged { language, _, _, _ ->
      viewModel.publish(SearchIntention.OnLanguageChanged(language = language.toString()))
    }

    search.setOnClickListener {
      viewModel.publish(SearchIntention.Search(language = language.text.toString()))
    }
  }

  private fun bindOutputs() {
    watch(viewModel.state) { state ->
      binding.search.isEnabled = state.isSearchEnabled
    }
  }

}
