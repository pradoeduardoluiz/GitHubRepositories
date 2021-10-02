package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val original: Request = chain.request()

    val newRequest = original.newBuilder()
      .header("Authorization", "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")
      .build()

    return chain.proceed(newRequest)
  }
}
