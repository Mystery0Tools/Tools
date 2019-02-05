package vip.mystery0.tools.base

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.Preference
import androidx.annotation.XmlRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat

abstract class BasePreferenceFragment(@XmlRes private val preferencesResId: Int) : PreferenceFragmentCompat() {
	val permissionArray: ArrayList<Array<String>> by lazy { ArrayList<Array<String>>() }
	val permissionMap: ArrayList<(Int, IntArray) -> Unit> by lazy { ArrayList<(Int, IntArray) -> Unit>() }

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

	fun <T : Preference> findPreferenceById(@StringRes id: Int): T {
		@Suppress("UNCHECKED_CAST")
		return findPreference(getString(id)) as T
	}

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