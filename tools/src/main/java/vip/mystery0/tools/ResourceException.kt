package vip.mystery0.tools

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class ResourceException(@StringRes val id: Int) : RuntimeException() {
	fun message(context: Context): String = context.getString(id)
}

fun Throwable?.toast(context: Context = context()) = dispatch(this, false, context)
fun Throwable?.toastLong(context: Context = context()) = dispatch(this, true, context)
fun String?.toast(context: Context = context()) = newToast(context, this, Toast.LENGTH_SHORT)
fun String?.toastLong(context: Context = context()) = newToast(context, this, Toast.LENGTH_LONG)

fun Throwable?.dispatchMessage(): String? = when (this) {
	is ResourceException -> this.message(context())
	else -> this?.message
}

private fun dispatch(throwable: Throwable?, isLong: Boolean, context: Context = context()) {
	newToast(context, throwable.dispatchMessage(), if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
}

private fun newToast(context: Context, message: String?, length: Int) {
	Toast.makeText(context, message, length).show()
}