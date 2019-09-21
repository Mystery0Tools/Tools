package vip.mystery0.cookie

import android.content.Context
import android.content.SharedPreferences
import vip.mystery0.tools.context

class CookieManager private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = CookieManager()
	}

	private val cookiePreferences: SharedPreferences by lazy {
		context().getSharedPreferences("cookie", Context.MODE_PRIVATE)
	}

	fun putCookie(domain: String, cookie: String?) = cookiePreferences.edit().putString(domain, cookie).apply()

	fun getCookie(domain: String): String? = cookiePreferences.getString(domain, null)
}