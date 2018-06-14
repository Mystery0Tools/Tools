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

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity(@LayoutRes private val layoutId: Int?) : AppCompatActivity() {

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
}