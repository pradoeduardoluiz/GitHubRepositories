package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service

import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto.PageDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

  @GET("search/repositories")
  suspend fun getRepositories(
    @Query("q") language: String = "language:kotlin",
    @Query("page") page: Int,
    @Query("perPage") perPage: Int,
    @Query("sort") sort: String = "stars"
  ): PageDTO

}
