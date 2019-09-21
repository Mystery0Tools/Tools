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

import vip.mystery0.tools.context

/**
 * dp转px
 * @param dpValue dp值
 * @return 转换之后的px值
 */
fun dpTopx(dpValue: Float): Int = (dpValue * context().resources.displayMetrics.density + 0.5).toInt()

/**
 * dp转px
 * @param dpValue dp值
 * @return 转换之后的px值
 */
fun dpTopx(dpValue: Int): Float = dpValue * context().resources.displayMetrics.density + 0.5f

/**
 * px转dp
 * @param pxValue px值
 * @return 转换之后的dp值
 */
fun pxTodp(pxValue: Float): Int = (pxValue / context().resources.displayMetrics.density + 0.5).toInt()

/**
 * px转dp
 * @param pxValue px值
 * @return 转换之后的dp值
 */
fun pxTodp(pxValue: Int): Float = pxValue / context().resources.displayMetrics.density + 0.5f

/**
 * 获取屏幕宽度
 * @return px值
 */
fun screenWidth(): Int = context().resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 * @return px值
 */
fun screenHeight(): Int = context().resources.displayMetrics.heightPixels

val screenWidth: Int = screenWidth()
val screenHeight: Int = screenHeight()