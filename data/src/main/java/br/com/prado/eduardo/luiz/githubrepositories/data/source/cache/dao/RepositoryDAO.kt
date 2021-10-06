package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dbo.RepositoryDBO

@Dao
abstract class RepositoryDAO : BaseDAO<RepositoryDBO>() {

  @Query(value = "SELECT * FROM repository WHERE language LIKE :language ORDER BY stars DESC, name ASC")
  abstract fun getRepositories(language: String): PagingSource<Int, RepositoryDBO>

  @Query(value = "SELECT COUNT(id) FROM repository WHERE language LIKE :language ORDER BY stars DESC, name ASC")
  abstract fun getRowCount(language: String): Int

  @Query("DELETE FROM repository")
  abstract fun deleteAll()
}
