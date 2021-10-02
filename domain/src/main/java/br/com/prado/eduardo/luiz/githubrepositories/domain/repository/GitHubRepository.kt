package br.com.prado.eduardo.luiz.githubrepositories.domain.repository

import br.com.prado.eduardo.luiz.domain.model.PageModel
import br.com.prado.eduardo.luiz.domain.model.RepositoryModel

interface GitHubRepository {

  suspend fun getRepositories(language: String, page: Int, perPage: Int): PageModel<RepositoryModel>

}
