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

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorTools {
	fun parseColor(@ColorInt color: Int): Int {
		return parseColor(color, alpha = 255)
	}

	fun parseColor(@ColorInt color: Int, alpha: Int): Int {
		return Color.parseColor(parseColorToString(color, alpha))
	}

	fun parseColorToString(@ColorInt color: Int): String {
		return parseColorToString(color, alpha = 255)
	}

	fun parseColorToString(@ColorInt color: Int, alpha: Int): String {
		val stringBuilder = StringBuilder()
		stringBuilder.append('#')
		val alphaString = Integer.toHexString(alpha).toUpperCase()
		stringBuilder.append(if (alphaString.length < 2) "0$alphaString" else alphaString)
		val redString = Integer.toHexString(Color.red(color)).toUpperCase()
		val greenString = Integer.toHexString(Color.green(color)).toUpperCase()
		val blueString = Integer.toHexString(Color.blue(color)).toUpperCase()
		stringBuilder.append(if (redString.length < 2) "0$redString" else redString)
		stringBuilder.append(if (greenString.length < 2) "0$greenString" else greenString)
		stringBuilder.append(if (blueString.length < 2) "0$blueString" else blueString)
		return stringBuilder.toString()
	}
}
