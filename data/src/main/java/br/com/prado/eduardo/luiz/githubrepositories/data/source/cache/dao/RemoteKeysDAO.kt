package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RemoteKeysDBO

@Dao
abstract class RemoteKeysDAO : BaseDAO<RemoteKeysDBO>() {

  @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
  abstract fun getById(repoId: Long): RemoteKeysDBO?

  @Query("DELETE FROM remote_keys")
  abstract fun deleteAll()
}
