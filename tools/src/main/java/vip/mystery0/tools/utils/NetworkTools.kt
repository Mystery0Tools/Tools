package vip.mystery0.tools.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import vip.mystery0.tools.ToolsClient

class NetworkTools private constructor() {
	companion object {
		val INSTANCE by lazy { Holder.holder }
		val instance = INSTANCE
	}

	private object Holder {
		val holder = NetworkTools()
	}

	private val connectivityManager by lazy { ToolsClient.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

	@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
	fun isConnectInternet(): Boolean {
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}
}