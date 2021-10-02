package br.com.prado.eduardo.luiz.githubrepositories.di

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.AppDispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun providerDispatchersProvider(): DispatchersProvider = AppDispatchersProvider()

}
