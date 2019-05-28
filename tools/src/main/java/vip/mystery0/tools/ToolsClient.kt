package vip.mystery0.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.getDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

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