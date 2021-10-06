package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache

import android.content.Context
import androidx.room.Room

class DatabaseHelper(private val context: Context) {

  fun buildDataBase(): Database {
    return Room.databaseBuilder(
      context,
      Database::class.java,
      DatabaseConstants.NAME
    ).apply {
      fallbackToDestructiveMigration()
    }.build()
  }

}
