package br.com.prado.eduardo.luiz.githubrepositories.di

import br.com.prado.eduardo.luiz.githubrepositories.dispachers.AppDispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.mappers.RepositoriesMapper
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import br.com.prado.eduardo.luiz.githubrepositories.navigation.NavigatorImpl
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

  @Provides
  @Singleton
  fun providerNavigator(
    dispatchersProvider: DispatchersProvider
  ): Navigator = NavigatorImpl(dispatchersProvider)

  @Provides
  @Singleton
  fun providerRepositoriesMapper(): RepositoriesMapper = RepositoriesMapper()

}
