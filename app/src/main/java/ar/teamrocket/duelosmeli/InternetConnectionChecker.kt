package ar.teamrocket.duelosmeli

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Esta función permite verificar si el dispositivo cuenta con conexión a internet.
 */
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
