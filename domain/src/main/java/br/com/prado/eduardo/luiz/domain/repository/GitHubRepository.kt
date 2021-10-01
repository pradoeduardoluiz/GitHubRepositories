package br.com.prado.eduardo.luiz.domain.repository

import br.com.prado.eduardo.luiz.domain.model.PageModel

interface GitHubRepository {

  suspend fun getRepositories(language: String, page: Int): PageModel

}
