package br.com.prado.eduardo.luiz.githubrepositories.di

import android.content.Context
import br.com.prado.eduardo.luiz.githubrepositories.connection.ConnectionStateMonitor
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.AppDispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.dispachers.DispatchersProvider
import br.com.prado.eduardo.luiz.githubrepositories.navigation.Navigator
import br.com.prado.eduardo.luiz.githubrepositories.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
  fun provideConnectionStateMonitor(@ApplicationContext context: Context): ConnectionStateMonitor {
    return ConnectionStateMonitor(context)
  }

}
