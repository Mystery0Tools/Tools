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

import java.io.File
import java.text.DecimalFormat

open class FileTools {
	companion object {
		@JvmStatic
		fun formatFileSize(file: File, decimalNum: Int = 2): String {
			return if (!file.exists()) "0B" else formatFileSize(file.length(), decimalNum)
		}

		@JvmStatic
		fun formatFileSize(fileSize: Long, decimalNum: Int = 2): String {
			val formatString = StringBuilder()
			formatString.append("#.")
			for (i in 0 until decimalNum)
				formatString.append('0')
			val decimalFormat = DecimalFormat(formatString.toString())
			val fileSizeString: String
			if (fileSize < 1024)
				fileSizeString = decimalFormat.format(fileSize) + 'B'
			else if (fileSize < 1024 * 1024)
				fileSizeString = decimalFormat.format(fileSize / 1024) + "KB"
			else if (fileSize < 1024 * 1024 * 1024)
				fileSizeString = decimalFormat.format(fileSize / 1024 / 1024) + "MB"
			else
				fileSizeString = decimalFormat.format(fileSize / 1024 / 1024 / 1024) + "GB"
			return fileSizeString
		}
	}
}
