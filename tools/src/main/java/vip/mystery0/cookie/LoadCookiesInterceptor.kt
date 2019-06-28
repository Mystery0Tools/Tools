package vip.mystery0.cookie

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

class LoadCookiesInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val builder = request.newBuilder()
		val cookie = CookieManager.instance.getCookie(request.url.host)
		if (!TextUtils.isEmpty(cookie)) builder.addHeader("Cookie", cookie!!)
		return chain.proceed(builder.build())
	}
}