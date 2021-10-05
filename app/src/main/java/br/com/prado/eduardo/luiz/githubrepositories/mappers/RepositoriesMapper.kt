package br.com.prado.eduardo.luiz.githubrepositories.mappers

import androidx.paging.PagingData
import androidx.paging.map
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.ui.repositories.RepositoriesState

class RepositoriesMapper {

  fun mapToState(pagingData: PagingData<RepositoryModel>): PagingData<RepositoriesState.Item> {
    return pagingData.map(::mapToState)
  }

  private fun mapToState(repository: RepositoryModel): RepositoriesState.Item {
    return RepositoriesState.Item(
      id = repository.id,
      name = repository.name,
      fullName = repository.fullName,
      isPrivate = repository.isPrivate,
      ownerName = repository.owner.name,
      ownerImageUrl = repository.owner.avatarUrl,
      description = repository.description,
      url = repository.url,
      forks = repository.forks,
      stars = repository.stars
    )
  }

}
