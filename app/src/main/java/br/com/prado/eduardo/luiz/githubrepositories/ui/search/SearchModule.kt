package br.com.prado.eduardo.luiz.githubrepositories.ui.search

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object SearchModule {
  @Provides
  @ViewModelScoped
  @SearchStateQualifier
  fun provideInitialState(): SearchState = SearchState()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchStateQualifier
