package vip.mystery0.tools.utils

import android.content.SharedPreferences
import vip.mystery0.tools.context

fun SharedPreferences.use(block: SharedPreferences.Editor.() -> Unit) {
	val editor = edit()
	block(editor)
	editor.apply()
}

fun sp(name: String, mode: Int): SharedPreferences = context().getSharedPreferences(name, mode)