package br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ResponseHeaderInterceptor(
  private val name: String,
  private val value: String
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())
    return response.newBuilder().header(name, value).build()
  }
}
