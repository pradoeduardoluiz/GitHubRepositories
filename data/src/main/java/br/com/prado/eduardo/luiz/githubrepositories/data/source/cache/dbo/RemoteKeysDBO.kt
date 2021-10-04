package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.DatabaseConstants

@Entity(tableName = DatabaseConstants.Table.REMOTE_KEYS)
data class RemoteKeysDBO(
  @PrimaryKey
  val repoId: Long,
  val prevKey: Int?,
  val nextKey: Int?
)
