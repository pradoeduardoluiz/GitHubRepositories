package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryDTO(
  @Json(name = "id") val id: Long,
  @Json(name = "name") val name: String,
  @Json(name = "full_name") val fullName: String,
  @Json(name = "private") val isPrivate: Boolean,
  @Json(name = "owner") val owner: OwnerDTO,
  @Json(name = "description") val description: String?,
  @Json(name = "url") val url: String,
  @Json(name = "forks") val forks: Long,
)
