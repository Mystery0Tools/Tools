package vip.mystery0.cookie

import okhttp3.Interceptor
import okhttp3.Response

class SaveCookiesInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		if (response.headers("set-cookie").isNotEmpty()) {
			val cookies = encodeCookie(response.headers("set-cookie"))
			CookieManager.putCookie(request.url().host(), cookies)
		}
		return response
	}


	//整合cookie为唯一字符串
	private fun encodeCookie(cookies: List<String>): String {
		val sb = StringBuilder()
		val set = HashSet<String>()
		cookies.map { cookie ->
			cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		}.forEach { arr ->
			arr.filterNot { set.contains(it) }.forEach { set.add(it) }
		}

		val ite = set.iterator()
		while (ite.hasNext()) {
			val cookie = ite.next()
			sb.append(cookie).append(';')
		}

		val last = sb.lastIndexOf(';')
		if (sb.length - 1 == last) {
			sb.deleteCharAt(last)
		}

		return sb.toString()
	}
}