package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.prado.eduardo.luiz.githubrepositories.R
import br.com.prado.eduardo.luiz.githubrepositories.databinding.RepositoryItemBinding
import coil.load

class RepositoriesAdapter :
  PagingDataAdapter<RepositoriesState.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = RepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RepositoryViewHolder(binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = getItem(position) ?: return
    if (holder is RepositoryViewHolder) holder.bind(item)
  }

  private inner class RepositoryViewHolder(
    private val binding: RepositoryItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RepositoriesState.Item) {
      with(binding) {
        image.load(item.ownerImageUrl)
        name.text = item.name
        author.text = item.ownerName
        stars.text = root.context.getString(R.string.label_stars, item.forks.toString())
        forks.text = root.context.getString(R.string.label_forks, item.forks.toString())
      }
    }
  }

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RepositoriesState.Item>() {
      override fun areItemsTheSame(
        oldItem: RepositoriesState.Item,
        newItem: RepositoriesState.Item
      ): Boolean = oldItem.id == newItem.id

      override fun areContentsTheSame(
        oldItem: RepositoriesState.Item,
        newItem: RepositoriesState.Item
      ): Boolean = oldItem == newItem
    }
  }

}
