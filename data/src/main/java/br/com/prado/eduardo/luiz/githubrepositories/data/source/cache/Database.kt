package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao.RepositoryDAO
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO

@Database(
  entities = [RepositoryDBO::class],
  version = DatabaseConstants.VERSION,
  exportSchema = false
)
abstract class Database : RoomDatabase() {
  abstract fun repositoryDao(): RepositoryDAO
}
