package vip.mystery0.tools.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT, context: Context = vip.mystery0.tools.context()) {
	Toast.makeText(context, text, duration).show()
}

fun toast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_SHORT, context: Context = vip.mystery0.tools.context()) {
	toast(context.getString(textRes), duration, context)
}

fun toastLong(text: CharSequence?, context: Context = vip.mystery0.tools.context()) {
	toast(text, Toast.LENGTH_LONG, context)
}

fun toastLong(@StringRes textRes: Int, context: Context = vip.mystery0.tools.context()) {
	toastLong(context.getString(textRes), context)
}