package vip.mystery0.tools

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ToolsClient {
	private lateinit var context: Context

	@JvmStatic
	fun getContext(): Context {
		if (::context.isInitialized)
			return context
		throw Exception("this function need context, please call “ToolsClient.initWithContext(Context)” first.")
	}

	@JvmStatic
	fun initWithContext(context: Context) {
		this.context = context.applicationContext
	}
}