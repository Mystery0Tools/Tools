package vip.mystery0.tools.utils

import android.view.View
import android.view.ViewGroup

fun <T : View> T.changeLayoutParams(action: (ViewGroup.LayoutParams) -> Unit) {
	val params = layoutParams
	action(params)
	layoutParams = params
}