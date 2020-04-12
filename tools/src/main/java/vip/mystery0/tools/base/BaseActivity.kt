/*
 * Created by Mystery0 on 18-3-20 下午2:01.
 * Copyright (c) 2018. All Rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.mystery0.tools.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

abstract class BaseActivity(@LayoutRes private val layoutId: Int?) : AppCompatActivity(), CoroutineScope by MainScope() {
	private val permissionArray by lazy { ArrayList<Array<String>>() }
	private val permissionMap by lazy { ArrayList<(Int, IntArray) -> Unit>() }
	private var toast: Toast? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (layoutId != null)
			inflateView(layoutId)
		bindView()
		initView()
		initData()
		loadDataToView()
		requestData()
		monitor()
	}

	open fun inflateView(layoutId: Int) {
		setContentView(layoutId)
	}

	open fun bindView() {}
	open fun initView() {}
	open fun initData() {}
	open fun loadDataToView() {}
	open fun requestData() {}
	open fun monitor() {}

	fun toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
		toast?.cancel()
		toast = Toast.makeText(this, text, duration)
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

	fun reRequestPermission(requestCode: Int) {
		if (requestCode < permissionMap.size)
			requestPermissions(permissionArray[requestCode], permissionMap[requestCode])
	}

	fun requestPermissions(permissionArray: Array<String>, requestResult: (Int, IntArray) -> Unit) {
		val needRequestPermissionArray = permissionArray.filter { !checkPermission(it) }
		if (needRequestPermissionArray.isEmpty()) {
			requestResult.invoke(-1, IntArray(0))
			return
		}
		val code = permissionMap.size
		this.permissionArray.add(permissionArray)
		permissionMap.add(requestResult)
		ActivityCompat.requestPermissions(this, needRequestPermissionArray.toTypedArray(), code)
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

	fun checkPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}