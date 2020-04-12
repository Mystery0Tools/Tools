package vip.mystery0.tools

import android.content.Context
import androidx.annotation.StringRes
import vip.mystery0.tools.utils.toast
import vip.mystery0.tools.utils.toastLong

class ResourceException(@StringRes val id: Int) : RuntimeException() {
	override val message: String?
		get() = context().getString(id)
}

fun Throwable?.toast(context: Context = context()) = dispatch(this, false, context)
fun Throwable?.toastLong(context: Context = context()) = dispatch(this, true, context)
fun String?.toast(context: Context = context()) = toast(this, context = context)
fun String?.toastLong(context: Context = context()) = toastLong(this, context = context)

private fun dispatch(throwable: Throwable?, isLong: Boolean, context: Context = context()) {
	throwable?.let {
		if (isLong) {
			toastLong(it.message, context = context)
		} else {
			toast(it.message, context = context)
		}
	}
}