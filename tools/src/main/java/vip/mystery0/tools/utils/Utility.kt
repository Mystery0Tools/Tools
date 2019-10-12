package vip.mystery0.tools.utils

import android.content.SharedPreferences

fun SharedPreferences.use(block: (SharedPreferences.Editor) -> Unit) {
	val editor = edit()
	block(editor)
	editor.apply()
}
