package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageDTO(
  @Json(name = "total_count") val totalCount: Int,
  @Json(name = "items") val repositories: List<RepositoryDTO>
)
