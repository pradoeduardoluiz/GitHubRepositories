package br.com.prado.eduardo.luiz.githubrepositories.ui.repositories

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(FragmentComponent::class, ViewModelComponent::class)
object RepositoriesModule {
  @Provides
  @FragmentScoped
  fun provideRepositoriesAdapter() = RepositoriesAdapter()

  @Provides
  @ViewModelScoped
  @RepositoriesStateQualifier
  fun provideInitialState(): RepositoriesState = RepositoriesState()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepositoriesStateQualifier
