package br.com.prado.eduardo.luiz.githubrepositories.data.source.cache

object DatabaseConstants {

  object Table {
    const val REPOSITORY = "repository"
    const val REMOTE_KEYS = "remote_keys"
  }

  const val VERSION = 1
  const val NAME = "github_repository_database.db"

  const val ROW_NOT_INSERTED = -1L
}
