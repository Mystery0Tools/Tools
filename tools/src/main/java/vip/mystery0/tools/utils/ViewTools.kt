package vip.mystery0.tools.utils

import android.view.View
import android.view.ViewGroup

fun View.changeLayoutParams(action: (ViewGroup.LayoutParams) -> Unit) = ViewTools.instance.changeLayoutParams(this, action)

class ViewTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = ViewTools()
	}

	fun changeLayoutParams(view: View, action: (ViewGroup.LayoutParams) -> Unit) {
		val params = view.layoutParams
		action(params)
		view.layoutParams = params
	}
}