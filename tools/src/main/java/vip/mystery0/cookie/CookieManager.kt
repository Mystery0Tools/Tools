package vip.mystery0.cookie

import android.content.Context
import android.content.SharedPreferences
import vip.mystery0.tools.ToolsClient

object CookieManager {
	private val cookiePreferences: SharedPreferences by lazy {
		ToolsClient.getContext().getSharedPreferences("cookie", Context.MODE_PRIVATE)
	}

	fun putCookie(domain: String, cookie: String?) = cookiePreferences.edit().putString(domain, cookie).apply()

	fun getCookie(domain: String): String? = cookiePreferences.getString(domain, null)
}