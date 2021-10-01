package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service

import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.PageDTO
import retrofit2.http.GET

interface GitHubService {

  @GET
  suspend fun getRepositories(page: Int): PageDTO

}
