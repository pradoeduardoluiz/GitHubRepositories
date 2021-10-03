package br.com.prado.eduardo.luiz.githubrepositories.domain.repository

import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

  suspend fun getRepositories(language: String): Flow<PagingData<RepositoryModel>>

}
