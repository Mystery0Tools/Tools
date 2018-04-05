package vip.mystery0.tools.base

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

abstract class BaseFragment : Fragment() {
	var TAG = javaClass.simpleName

	fun toastMessage(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
		toastMessage(getString(id), duration)
	}

	fun toastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
		Toast.makeText(context, message, duration).show()
	}
}