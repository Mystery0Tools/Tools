package vip.mystery0.tools.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import vip.mystery0.tools.context

@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun isConnectInternet(): Boolean {
	val connectivityManager = context().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val activeNetworkInfo = connectivityManager.activeNetworkInfo
	return activeNetworkInfo != null && activeNetworkInfo.isConnected
}