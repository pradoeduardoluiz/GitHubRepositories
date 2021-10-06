package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerDTO(
  @Json(name = "login") val name: String,
  @Json(name = "avatar_url") val avatarUrl: String
)
