package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao.RemoteKeysDAO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao.RepositoryDAO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RemoteKeysDBO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO

@Database(
  entities = [
    RepositoryDBO::class,
    RemoteKeysDBO::class
  ],
  version = DatabaseConstants.VERSION,
  exportSchema = false
)
abstract class Database : RoomDatabase() {
  abstract fun repositoryDao(): RepositoryDAO
  abstract fun remoteKeysDao(): RemoteKeysDAO
}
