package vip.mystery0.cookie

import android.content.Context
import android.content.SharedPreferences
import vip.mystery0.tools.utils.sp
import vip.mystery0.tools.utils.use

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
		sp("cookie", Context.MODE_PRIVATE)
	}

	fun putCookie(domain: String, cookie: String?) = cookiePreferences.use {
		putString(domain, cookie)
	}

	fun getCookie(domain: String): String? = cookiePreferences.getString(domain, null)
}