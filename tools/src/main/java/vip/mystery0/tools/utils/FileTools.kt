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

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Base64
import java.io.*
import java.text.DecimalFormat

object FileTools {
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	fun getPath(context: Context, uri: Uri): String? {
		when {
			DocumentsContract.isDocumentUri(context, uri) -> {
				when (uri.authority) {
					"com.android.externalstorage.documents" -> {
						val docId = DocumentsContract.getDocumentId(uri)
						val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
						val type = split[0]

						if ("primary".equals(type, ignoreCase = true)) {
							return Environment.getExternalStorageDirectory().toString() + File.pathSeparator + split[1]
						}
					}
					"com.android.providers.downloads.documents" -> {
						val id = DocumentsContract.getDocumentId(uri)
						val contentUri = ContentUris.withAppendedId(
								Uri.parse("content://downloads/public_downloads"), id.toLong())

						return getDataColumn(context, contentUri, null, null)
					}
					"com.android.providers.media.documents" -> {
						val docId = DocumentsContract.getDocumentId(uri)
						val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
						val type = split[0]

						var contentUri: Uri? = null
						when (type) {
							"image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
							"video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
							"audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
						}

						val selection = "_id=?"
						val selectionArgs = arrayOf(split[1])

						return getDataColumn(context, contentUri!!, selection, selectionArgs)
					}
				}
			}
			"content".equals(uri.scheme, ignoreCase = true) ->
				return getDataColumn(context, uri, null, null)
			"file".equals(uri.scheme, ignoreCase = true) ->
				return uri.path
		}
		return null
	}

	private fun getDataColumn(context: Context, uri: Uri, selection: String?,
							  selectionArgs: Array<String>?): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)
		try {
			cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
			if (cursor != null && cursor.moveToFirst()) {
				val columnIndex = cursor.getColumnIndexOrThrow(column)
				return cursor.getString(columnIndex)
			}
		} finally {
			if (cursor != null)
				cursor.close()
		}
		return null
	}

	fun bitmapToByteArray(compressFormat: Bitmap.CompressFormat, bitmap: Bitmap): ByteArray {
		val byteArrayOutputStream = ByteArrayOutputStream()
		bitmap.compress(compressFormat, 100, byteArrayOutputStream)
		return byteArrayOutputStream.toByteArray()
	}

	fun compressImage(compressFormat: Bitmap.CompressFormat, file: File, maxSize: Int, interval: Int): String {
		if (interval <= 0 || interval > 100)
			throw NumberFormatException("interval can not be less 0 or more than 100")
		val outputStream = ByteArrayOutputStream()
		var option = 100
		var base64String: String
		while (true) {
			outputStream.reset()
			val bitmap = BitmapFactory.decodeFile(file.absolutePath)
			bitmap.compress(compressFormat, option, outputStream)
			base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
			if (base64String.length <= maxSize || option <= 0)
				break
			option -= interval
		}
		outputStream.close()
		return base64String
	}

	fun formatFileSize(file: File): String {
		return formatFileSize(file, decimalNum = 2)
	}

	fun formatFileSize(file: File, decimalNum: Int): String {
		return if (!file.exists()) "0B" else formatFileSize(file.length(), decimalNum)
	}

	fun formatFileSize(fileSize: Long): String {
		return formatFileSize(fileSize, decimalNum = 2)
	}

	fun formatFileSize(fileSize: Long, decimalNum: Int): String {
		val formatString = StringBuilder()
		formatString.append("#.")
		for (i in 0 until decimalNum)
			formatString.append('0')
		val decimalFormat = DecimalFormat(formatString.toString())
		val fileSizeString: String
		fileSizeString = when {
			fileSize < 1024 -> decimalFormat.format(fileSize) + 'B'
			fileSize < 1024 * 1024 -> decimalFormat.format(fileSize / 1024) + "KB"
			fileSize < 1024 * 1024 * 1024 -> decimalFormat.format(fileSize / 1024 / 1024) + "MB"
			else -> decimalFormat.format(fileSize / 1024 / 1024 / 1024) + "GB"
		}
		return fileSizeString
	}

	const val DONE = 100
	const val ERROR = 101
	const val FILE_NOT_EXIST = 102
	const val MAKE_DIR_ERROR = 103

	/**
	 * 拷贝文件
	 *
	 * @param inputPath  输入路径
	 * @param outputPath 输出路径
	 * @return 返回码
	 */
	fun copyFile(inputPath: String, outputPath: String): Int {
		if (!File(inputPath).exists()) {
			return FILE_NOT_EXIST
		}
		val outputParentFile = File(outputPath).parentFile
		if (!outputParentFile.exists() && !outputParentFile.mkdirs())
			return MAKE_DIR_ERROR
		var fileInputStream: FileInputStream? = null
		var fileOutputStream: FileOutputStream? = null
		try {
			fileInputStream = FileInputStream(inputPath)
			fileOutputStream = FileOutputStream(outputPath)
			val bytes = ByteArray(1024 * 1024 * 10)
			var readCount = 0
			while (readCount != -1) {
				fileOutputStream.write(bytes, 0, readCount)
				readCount = fileInputStream.read(bytes)
			}
			return DONE
		} catch (e: Exception) {
			return ERROR
		} finally {
			if (fileInputStream != null)
				fileInputStream.close()
			if (fileOutputStream != null)
				fileOutputStream.close()
		}
	}
}
