package br.com.prado.eduardo.luiz.githubrepositories.mappers

import androidx.paging.PagingData
import androidx.paging.map
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.ui.repositories.RepositoriesState

fun PagingData<RepositoryModel>.mapToState(): PagingData<RepositoriesState.Item> {
  return this.map { repository -> repository.mapToState() }
}

fun RepositoryModel.mapToState(): RepositoriesState.Item {
  return RepositoriesState.Item(
    id = id,
    name = name,
    fullName = fullName,
    isPrivate = isPrivate,
    ownerName = owner.name,
    ownerImageUrl = owner.avatarUrl,
    description = description,
    url = url,
    forks = forks,
    stars = stars
  )
}
