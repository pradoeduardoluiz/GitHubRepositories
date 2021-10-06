package br.com.prado.eduardo.luiz.githubrepositories.data.di

import android.content.Context
import android.content.SharedPreferences
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.AppPreferences
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

  @Provides
  @Singleton
  fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
    return PreferencesHelper.createPrefs(context, PreferencesHelper.APP_PREFERENCES)
  }

  @Provides
  @Singleton
  fun provideAppPreferences(sharedPreferences: SharedPreferences): AppPreferences {
    return AppPreferences(sharedPreferences)
  }

}
