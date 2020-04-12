package vip.mystery0.tools.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

abstract class BasePreferenceFragment(@XmlRes private val preferencesResId: Int) : PreferenceFragmentCompat() {
	private val permissionArray by lazy { ArrayList<Array<String>>() }
	private val permissionMap by lazy { ArrayList<(Int, IntArray) -> Unit>() }
	private var toast: Toast? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(preferencesResId, rootKey)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		initPreference()
		monitor()
	}

	open fun initPreference() {}

	open fun monitor() {}

	fun toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
		toast?.cancel()
		toast = Toast.makeText(context, text, duration)
		toast?.show()
	}

	fun toast(@StringRes textRes: Int, duration: Int = Toast.LENGTH_SHORT) {
		toast(getString(textRes), duration)
	}

	fun toastLong(text: CharSequence?) {
		toast(text, Toast.LENGTH_LONG)
	}

	fun toastLong(@StringRes textRes: Int) {
		toastLong(getString(textRes))
	}

	fun <T : Preference> findPreferenceById(@StringRes id: Int): T = findPreference(getString(id))!!

	fun reRequestPermission(requestCode: Int) {
		if (requestCode < permissionMap.size)
			requestPermissionsOnFragment(permissionArray[requestCode], permissionMap[requestCode])
	}

	fun requestPermissionsOnFragment(permissionArray: Array<String>, requestResult: (Int, IntArray) -> Unit) {
		val needRequestPermissionArray = permissionArray.filter { !checkPermission(it) }
		if (needRequestPermissionArray.isEmpty()) {
			requestResult.invoke(-1, IntArray(0))
			return
		}
		val code = permissionMap.size
		this.permissionArray.add(permissionArray)
		permissionMap.add(requestResult)
		requestPermissions(needRequestPermissionArray.toTypedArray(), code)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode < permissionMap.size)
			permissionMap[requestCode].invoke(requestCode, grantResults)
	}

	override fun onDestroy() {
		permissionArray.clear()
		permissionMap.clear()
		super.onDestroy()
	}

	fun checkPermissions(permissions: Array<out String>): BooleanArray = permissions.map { checkPermission(it) }.toBooleanArray()

	fun checkPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED
}