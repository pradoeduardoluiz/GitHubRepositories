package br.com.prado.eduardo.luiz.githubrepositories.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.IntRange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

class ConnectionStateMonitor constructor(context: Context) {
  private val connectivityManager = context.applicationContext.getSystemService(
    Context.CONNECTIVITY_SERVICE
  ) as ConnectivityManager

  // general availability of Internet over any type
  private var isOnline = false
    get() {
      updateFields()
      return field
    }

  private var isOverWifi = false
    get() {
      updateFields()
      return field
    }

  private var isOverCellular = false
    get() {
      updateFields()
      return field
    }

  private var isOverEthernet = false
    get() {
      updateFields()
      return field
    }

  private fun updateFields() {
    val networkAvailability = connectivityManager.getNetworkCapabilities(
      connectivityManager.activeNetwork
    )

    if (networkAvailability != null &&
      networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
      networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    ) {
      // has network
      isOnline = true

      // wifi
      isOverWifi = networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

      // cellular
      isOverCellular = networkAvailability
        .hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

      // ethernet
      isOverEthernet = networkAvailability
        .hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } else {
      isOnline = false
      isOverWifi = false
      isOverCellular = false
      isOverEthernet = false
    }
  }

  fun watchNetwork(): Flow<Boolean> = watchWifi()
    .combine(watchCellular()) { wifi, cellular -> wifi || cellular }
    .combine(watchEthernet()) { wifiAndCellular, ethernet -> wifiAndCellular || ethernet }

  fun watchWifi(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_WIFI)

  fun watchCellular(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_CELLULAR)

  fun watchEthernet(): Flow<Boolean> = callbackFlowForType(NetworkCapabilities.TRANSPORT_ETHERNET)

  private fun callbackFlowForType(@IntRange(from = 0, to = 7) type: Int): Flow<Boolean> {

    fun hasInitialConnectionAvailable(type: Int): Boolean {
      return when (type) {
        NetworkCapabilities.TRANSPORT_WIFI -> isOverWifi
        NetworkCapabilities.TRANSPORT_CELLULAR -> isOverCellular
        NetworkCapabilities.TRANSPORT_ETHERNET -> isOverEthernet
        else -> false
      }
    }

    return callbackFlow {

      this.trySend(hasInitialConnectionAvailable(type)).isSuccess

      val networkRequest = NetworkRequest.Builder()
        .addTransportType(type)
        .build()

      val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
          this@callbackFlow.trySend(false).isSuccess
        }

        override fun onUnavailable() {
          this@callbackFlow.trySend(false).isSuccess
        }

        override fun onLosing(network: Network, maxMsToLive: Int) = Unit

        override fun onAvailable(network: Network) {
          this@callbackFlow.trySend(true).isSuccess
        }
      }

      connectivityManager.registerNetworkCallback(networkRequest, callback)

      awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
  }
}
