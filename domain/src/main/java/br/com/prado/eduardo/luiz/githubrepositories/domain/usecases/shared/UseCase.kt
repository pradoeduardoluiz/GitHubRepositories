package br.com.prado.eduardo.luiz.githubrepositories.domain.usecases.shared

interface UseCase<Params, Result> {
  suspend operator fun invoke(params: Params): Result
}
