package br.com.prado.eduardo.luiz.githubrepositories.data.di

import android.content.Context
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.DatabaseHelper
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.dao.RepositoryDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

  @Provides
  @Singleton
  fun provideDatabaseHelper(@ApplicationContext context: Context): Database {
    return DatabaseHelper(context).buildDataBase()
  }

  @Provides
  @Singleton
  fun provideAccountDAO(database: Database): RepositoryDAO {
    return database.repositoryDao()
  }

}
