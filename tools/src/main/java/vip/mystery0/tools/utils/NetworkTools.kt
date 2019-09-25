package vip.mystery0.tools.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import vip.mystery0.tools.context

@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun isConnectInternet(): Boolean {
	val connectivityManager = context().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	if (sdkIsBefore(AndroidVersionCode.VERSION_Q)) {
		val networkInfo = connectivityManager.activeNetworkInfo
		return networkInfo != null && networkInfo.isConnected
	}
	val networks = connectivityManager.allNetworks
	if (networks.isNotEmpty()) {
		for (network in networks) {
			val nc = connectivityManager.getNetworkCapabilities(network)
			if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
				return true
			}
		}
	}
	return false
}