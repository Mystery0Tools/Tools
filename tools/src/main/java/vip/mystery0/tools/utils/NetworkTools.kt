package vip.mystery0.tools.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import vip.mystery0.tools.doByTry
import vip.mystery0.tools.getSystemService
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*


@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun isConnectInternet(): Boolean {
	val connectivityManager = getSystemService(ConnectivityManager::class.java) ?: return false
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

@RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
fun getLocalInetAddress(): InetAddress? {
	// WifiManagerPotentialLeak
	val wifiManager: WifiManager = getSystemService(WifiManager::class.java) ?: return null
	val wifiInfo = wifiManager.connectionInfo
	if (wifiInfo != null) {
		// WifiStateMachine doesn't support IPv6 as of P, so no need to get the original
		// InetAddress object with reflection.
		val addressInt = wifiInfo.ipAddress
		if (addressInt != 0) {
			return intToInetAddress(addressInt)
		}
	}
	doByTry {
		val networkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
		while (networkInterfaces.hasMoreElements()) {
			val networkInterface: NetworkInterface = networkInterfaces.nextElement()
			if (!networkInterface.isUp || networkInterface.isLoopback) {
				continue
			}
			val inetAddresses: Enumeration<InetAddress> = networkInterface.inetAddresses
			while (inetAddresses.hasMoreElements()) {
				val inetAddress: InetAddress = inetAddresses.nextElement()
				// Works for consumer IPv4 addresses.
				if (!inetAddress.isSiteLocalAddress) {
					continue
				}
				return@doByTry inetAddress
			}
		}
	}
	return null
}

/*
     * @see android.net.NetworkUtils#intToInetAddress(int)
     */
private fun intToInetAddress(hostAddress: Int): InetAddress {
	val addressBytes = byteArrayOf(
			(0xff and hostAddress).toByte(),
			(0xff and (hostAddress shr 8)).toByte(),
			(0xff and (hostAddress shr 16)).toByte(),
			(0xff and (hostAddress shr 24)).toByte()
	)
	return try {
		InetAddress.getByAddress(addressBytes)
	} catch (e: UnknownHostException) {
		throw AssertionError()
	}
}