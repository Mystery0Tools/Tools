package vip.mystery0.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getTColor(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.getTDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun Context.getTStringArray(@ArrayRes resId: Int): Array<String> = this.resources.getStringArray(resId)

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