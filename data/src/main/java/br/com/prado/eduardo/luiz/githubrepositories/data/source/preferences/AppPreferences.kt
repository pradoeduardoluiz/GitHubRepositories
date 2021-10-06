package br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences

import android.content.SharedPreferences
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.PreferencesHelper.get
import br.com.prado.eduardo.luiz.githubrepositories.data.source.preferences.PreferencesHelper.set

class AppPreferences(private val prefs: SharedPreferences) {

  var lastUpdate: Long
    get() = prefs[LAST_UPDATE_AT] ?: 0
    set(value) {
      prefs[LAST_UPDATE_AT] = value
    }

  companion object {
    const val LAST_UPDATE_AT = "last_update_at"
  }

}
