package vip.mystery0.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

private const val TAG = "ToolsClient"

fun context(): Context = ToolsClient.getContext()

fun getTString(@StringRes resId: Int) = context().getString(resId)
fun getTColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context(), resId)
fun getTDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(context(), resId)
fun getTStringArray(@ArrayRes resId: Int): Array<String> = context().resources.getStringArray(resId)

fun <R> doByTry(`try`: (Unit) -> R): Pair<R?, Exception?> = Unit.doByTry(`try`)
fun <R> doByTry(`try`: (Unit) -> R, finally: (() -> Unit)?): Pair<R?, Exception?> = Unit.doByTry(`try`, finally)

fun <T : Any?, R> T.doByTry(`try`: (T) -> R): Pair<R?, Exception?> = doByTry(`try`, null)
fun <T : Any?, R> T.doByTry(`try`: (T) -> R, finally: (() -> Unit)?): Pair<R?, Exception?> = try {
	val r = `try`(this)
	Pair(r, null)
} catch (e: Exception) {
	Pair(null, e)
} finally {
	finally?.invoke()
}

fun <R> doNoException(`try`: (Unit) -> R): R? = Unit.doNoException(`try`)
fun <R> doNoException(`try`: (Unit) -> R, finally: (() -> Unit)?): R? = Unit.doNoException(`try`, finally)

fun <T : Any?, R> T.doNoException(`try`: (T) -> R): R? = doNoException(`try`, null)
fun <T : Any?, R> T.doNoException(`try`: (T) -> R, finally: (() -> Unit)?): R? = try {
	`try`(this)
} catch (e: Exception) {
	Log.e(TAG, "doNoException: ", e)
	null
} finally {
	finally?.invoke()
}

fun tryOrBoolean(`try`: (Unit) -> Unit) = Unit.tryOrBoolean(`try`)
fun tryOrBoolean(`try`: (Unit) -> Unit, finally: (() -> Unit)?) = Unit.tryOrBoolean(`try`, finally)

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