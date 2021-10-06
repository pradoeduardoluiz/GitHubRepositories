package br.com.prado.eduardo.luiz.githubrepositories

import android.app.Application
import br.com.prado.eduardo.luiz.githubrepositories.data.source.remote.interceptor.ResponseHeaderInterceptor
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File

@HiltAndroidApp
class GitHubRepositoriesApp : Application(), ImageLoaderFactory {

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this)
      .availableMemoryPercentage(0.25)
      .crossfade(true)
      .okHttpClient {
        val cacheDirectory = File(filesDir, "image_cache").apply { mkdirs() }
        val cache = Cache(cacheDirectory, Long.MAX_VALUE)

        val cacheControlInterceptor =
          ResponseHeaderInterceptor("Cache-Control", "max-age=31536000,public")

        val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

        OkHttpClient.Builder()
          .cache(cache)
          .dispatcher(dispatcher)
          .addNetworkInterceptor(cacheControlInterceptor)
          .build()
      }
      .build()
  }
}

