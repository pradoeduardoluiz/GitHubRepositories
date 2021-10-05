package br.com.prado.eduardo.luiz.githubrepositories.data.di

import br.com.prado.eduardo.luiz.githubrepositories.data.repositories.GitHubRepositoryImpl
import br.com.prado.eduardo.luiz.githubrepositories.data.source.cache.Database
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.AppPreferences
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.service.GitHubService
import br.com.prado.eduardo.luiz.githubrepositories.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

  @Provides
  @Singleton
  fun provideGitHubRepository(
    gitHubService: GitHubService,
    database: Database,
    appPreferences: AppPreferences
  ): GitHubRepository =
    GitHubRepositoryImpl(gitHubService, database, appPreferences)

}
