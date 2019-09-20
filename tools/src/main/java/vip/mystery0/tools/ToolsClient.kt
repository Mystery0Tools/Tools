package vip.mystery0.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import vip.mystery0.tools.model.Pair2

private const val TAG = "ToolsClient"

fun context(): Context = ToolsClient.getContext()

fun getTColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context(), resId)
fun getTDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(context(), resId)
fun getTStringArray(@ArrayRes resId: Int): Array<String> = context().resources.getStringArray(resId)

fun <T : Any?, R> T.doByTry(`try`: (T) -> R): Pair2<R?, Exception?> = doByTry(`try`, null)

fun <T : Any?, R> T.doByTry(`try`: (T) -> R, finally: (() -> Unit)?): Pair2<R?, Exception?> = try {
	val r = `try`(this)
	Pair2(r, null)
} catch (e: Exception) {
	Pair2(null, e)
} finally {
	finally?.invoke()
}

fun <T : Any?, R> T.doNoException(`try`: (T) -> R): R? = doNoException(`try`, null)

fun <T : Any?, R> T.doNoException(`try`: (T) -> R, finally: (() -> Unit)?): R? = try {
	`try`(this)
} catch (e: Exception) {
	Log.e(TAG, "doNoException: ", e)
	null
} finally {
	finally?.invoke()
}

fun <T : Any?> T.tryOrBoolean(`try`: (T) -> Unit): Boolean = tryOrBoolean(`try`, null)

fun <T : Any?> T.tryOrBoolean(`try`: (T) -> Unit, finally: (() -> Unit)?): Boolean = try {
	`try`(this)
	true
} catch (e: Exception) {
	Log.e(TAG, "tryOrBoolean: ", e)
	false
} finally {
	finally?.invoke()
}

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