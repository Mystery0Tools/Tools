package vip.mystery0.tools.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import vip.mystery0.tools.ToolsClient

object NetworkTools {
	private val connectivityManager by lazy { ToolsClient.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

	@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
	fun isConnectInternet(): Boolean {
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}
}