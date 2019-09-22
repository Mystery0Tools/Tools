package vip.mystery0.tools.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {
	private var rootView: View? = null
	private val permissionArray: ArrayList<Array<String>> by lazy { ArrayList<Array<String>>() }
	private val permissionMap: ArrayList<(Int, IntArray) -> Unit> by lazy { ArrayList<(Int, IntArray) -> Unit>() }
	private var toast: Toast? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		if (rootView == null)
			rootView = inflateView(layoutId, inflater, container)
		return rootView
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		initView()
		monitor()
	}

	abstract fun initView()

	open fun inflateView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?): View {
		return inflater.inflate(layoutId, container, false)
	}

	open fun monitor() {}

	fun String?.toast(showLong: Boolean) {
		if (this != null)
			toastMessage(this, showLong)
	}

	fun toastMessage(text: String, showLong: Boolean = false) {
		toast?.cancel()
		toast = Toast.makeText(context, text, if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
		toast?.show()
	}

	fun <T : View> findViewById(@IdRes id: Int): T {
		return rootView!!.findViewById(id)
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