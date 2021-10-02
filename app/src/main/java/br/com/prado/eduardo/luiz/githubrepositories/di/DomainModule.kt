package br.com.prado.eduardo.luiz.githubrepositories.di

import br.com.prado.eduardo.luiz.domain.repository.GitHubRepository
import br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.GetRepositoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

  @Provides
  @Singleton
  fun providerGetRepositoriesUseCase(gitHubRepository: GitHubRepository) =
    GetRepositoriesUseCase(gitHubRepository = gitHubRepository)

}
