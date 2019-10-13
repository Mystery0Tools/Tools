package vip.mystery0.tools.progress

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class DownloadProgressInterceptor(private val listener: (Long, Long) -> Unit) : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalResponse = chain.proceed(chain.request())

		return originalResponse.newBuilder()
				.body(ProgressResponseBody(originalResponse.body!!, listener))
				.build()
	}
}