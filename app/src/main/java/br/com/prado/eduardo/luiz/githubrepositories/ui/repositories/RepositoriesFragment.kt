package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.prado.eduardo.luiz.githubrepositories.R

class RepositoriesFragment : Fragment() {

  companion object {
    fun newInstance() = RepositoriesFragment()
  }

  private lateinit var viewModel: RepositoriesViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.repositories_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(RepositoriesViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
