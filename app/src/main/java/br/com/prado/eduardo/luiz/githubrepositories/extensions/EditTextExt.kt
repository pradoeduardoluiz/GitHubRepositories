package br.com.prado.eduardo.luiz.githubrepositories.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setEditorSearchActionListener(onSearch: () -> Unit) {
  setOnEditorActionListener { _, actionId, _ ->
    return@setOnEditorActionListener when (actionId) {
      EditorInfo.IME_ACTION_SEARCH -> {
        onSearch()
        true
      }
      else -> false
    }
  }
}
