package vip.mystery0.tools.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar

abstract class BasePreferenceFragment(@XmlRes private val preferencesResId: Int) : PreferenceFragmentCompat() {
	private val permissionArray: ArrayList<Array<String>> by lazy { ArrayList<Array<String>>() }
	private val permissionMap: ArrayList<(Int, IntArray) -> Unit> by lazy { ArrayList<(Int, IntArray) -> Unit>() }
	private var toast: Toast? = null
	private var snackBar: Snackbar? = null
	private lateinit var snackBarView: View

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(preferencesResId, rootKey)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		snackBarView = getSnackBarView()
		initPreference()
		monitor()
	}

	open fun initPreference() {}

	abstract fun getSnackBarView(): View

	open fun monitor() {}

	fun String?.toast(showLong: Boolean) {
		if (this != null)
			toastMessage(this, showLong)
	}

	fun String?.snackBar(showLong: Boolean) {
		if (this != null)
			snackBarMessage(this, showLong)
	}

	fun toastMessage(text: String, showLong: Boolean = false) {
		toast?.cancel()
		toast = Toast.makeText(context, text, if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
		toast?.show()
	}

	fun snackBarMessage(text: String, showLong: Boolean = false) {
		snackBar?.dismiss()
		snackBar = Snackbar.make(snackBarView, text, if (showLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
		snackBar?.show()
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