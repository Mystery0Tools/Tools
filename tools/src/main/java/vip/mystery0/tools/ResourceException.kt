package vip.mystery0.tools

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class ResourceException(@StringRes val id: Int) : RuntimeException() {
	fun message(context: Context): String = context.getString(id)
}

fun Throwable?.toast(context: Context = ToolsClient.getContext()) = dispatch(this, false, context)
fun Throwable?.toastLong(context: Context = ToolsClient.getContext()) = dispatch(this, true, context)
fun String?.toast(context: Context = ToolsClient.getContext()) = newToast(context, this, Toast.LENGTH_SHORT)
fun String?.toastLong(context: Context = ToolsClient.getContext()) = newToast(context, this, Toast.LENGTH_LONG)

fun Throwable?.dispatchMessage(): String? = when (this) {
	is ResourceException -> this.message(ToolsClient.getContext())
	is ToolsException -> "error code: $code, message: $message"
	else -> this?.message
}

private fun dispatch(throwable: Throwable?, isLong: Boolean, context: Context = ToolsClient.getContext()) {
	newToast(context, throwable.dispatchMessage(), if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
}

private fun newToast(context: Context, message: String?, length: Int) {
	Toast.makeText(context, message, length).show()
}