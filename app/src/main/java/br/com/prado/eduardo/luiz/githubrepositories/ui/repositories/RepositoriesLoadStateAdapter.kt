package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoryLoadStateFooterItemBinding
import br.com.prado.eduardo.luiz.githubrepositories.extensions.isShimmering

class RepositoriesLoadStateAdapter(
  private val retry: () -> Unit
) : LoadStateAdapter<RepositoriesLoadStateAdapter.ViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    loadState: LoadState
  ): ViewHolder {
    val binding = RepositoryLoadStateFooterItemBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) = holder.bind(loadState)

  inner class ViewHolder(
    private val binding: RepositoryLoadStateFooterItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    init {
      binding.error.retry.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
      with(binding) {
        if (loadState is LoadState.Error) error.message.text = loadState.error.localizedMessage
        shimmer.isShimmering = loadState is LoadState.Loading
        error.root.isVisible = loadState is LoadState.Error
      }
    }
  }

}
