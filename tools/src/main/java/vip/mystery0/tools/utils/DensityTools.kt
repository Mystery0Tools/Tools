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

package vip.mystery0.tools.utils

import android.content.Context

object DensityTools {
	/**
	 * dp转px
	 * @param context 上下文
	 * @param dpValue dp值
	 * @return 转换之后的px值
	 */
	fun dp2px(context: Context, dpValue: Float): Int {
		return (dpValue * context.resources.displayMetrics.density + 0.5).toInt()
	}

	/**
	 * px转dp
	 * @param context 上下文
	 * @param pxValue px值
	 * @return 转换之后的dp值
	 */
	fun px2dp(context: Context, pxValue: Float): Int {
		return (pxValue / context.resources.displayMetrics.density + 0.5).toInt()
	}

	/**
	 * 获取屏幕宽度
	 * @param context 上下文
	 * @return px值
	 */
	fun getScreenWidth(context: Context): Int {
		return context.resources.displayMetrics.widthPixels
	}


	/**
	 * 获取屏幕高度
	 * @param context 上下文
	 * @return px值
	 */
	fun getScreenHeight(context: Context): Int {
		return context.resources.displayMetrics.heightPixels
	}
}
