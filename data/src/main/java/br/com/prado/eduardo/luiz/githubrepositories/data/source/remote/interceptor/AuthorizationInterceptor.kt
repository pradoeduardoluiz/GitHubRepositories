package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.interceptor

import br.com.prado.eduardo.luiz.githubrepositories.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val original: Request = chain.request()

    if (BuildConfig.API_TOKEN.isEmpty()) return chain.proceed(original)

    val newRequest = original.newBuilder()
      .header("Authorization", "token ${BuildConfig.API_TOKEN}")
      .build()

    return chain.proceed(newRequest)
  }
}
