package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.DatabaseConstants

@Entity(tableName = DatabaseConstants.Table.REPOSITORY)
data class RepositoryDBO(
  @PrimaryKey
  val id: Long,
  val name: String,
  val fullName: String,
  val isPrivate: Boolean,
  val ownerName: String,
  val ownerAvatarUrl: String,
  val description: String?,
  val url: String,
  val forks: Long,
  val stars: Long,
  val language: String,
)
