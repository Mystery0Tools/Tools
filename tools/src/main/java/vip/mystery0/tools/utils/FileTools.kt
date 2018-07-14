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

import android.content.ContentResolver
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
import androidx.annotation.RequiresApi
import android.util.Base64
import java.io.*
import java.text.DecimalFormat


object FileTools {
	/**
	 * 从uri中获取路径
	 * @param context 上下文
	 * @param uri 需要获取路径的uri对象
	 * @return 提取到的路径
	 */
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	fun getPath(context: Context, uri: Uri): String? {
		when (uri.scheme) {
			ContentResolver.SCHEME_FILE -> return uri.path
			ContentResolver.SCHEME_CONTENT -> {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
					var path: String? = null
					val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
					if (cursor != null) {
						if (cursor.moveToFirst()) {
							val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
							if (columnIndex > -1)
								path = cursor.getString(columnIndex)
						}
						cursor.close()
					}
					return path
				} else if (DocumentsContract.isDocumentUri(context, uri)) {
					when (uri.authority) {
						"com.android.externalstorage.documents" -> {
							val docId = DocumentsContract.getDocumentId(uri)
							val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
							val type = split[0]
							if (type.toLowerCase() == "primary")
								return Environment.getExternalStorageDirectory().toString() + File.pathSeparator + split[1]
						}
						"com.android.providers.downloads.documents" -> {
							val id = DocumentsContract.getDocumentId(uri)
							val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id.toLong())
							return getDataColumn(context, contentUri, null, null)
						}
						"com.android.providers.media.documents" -> {
							val docId = DocumentsContract.getDocumentId(uri)
							val split = docId.split(':')
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
			}
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
			cursor?.close()
		}
		return null
	}

	/**
	 * bitmap转成byte数组
	 * @param compressFormat 需要转换的格式
	 * @param bitmap 需要转换的bitmap
	 * @return 转换后的byte数组
	 */
	fun bitmapToByteArray(compressFormat: Bitmap.CompressFormat, bitmap: Bitmap): ByteArray {
		val byteArrayOutputStream = ByteArrayOutputStream()
		bitmap.compress(compressFormat, 100, byteArrayOutputStream)
		return byteArrayOutputStream.toByteArray()
	}

	/**
	 * 压缩图片并进行base64加密
	 * @param compressFormat 压缩的格式
	 * @param file 图片文件
	 * @param maxSize 压缩之后最大的大小
	 * @param interval 每次压缩的压缩率差值
	 * @return base64字符串
	 */
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

	/**
	 * 格式化文件大小
	 * @param file 需要格式化的文件
	 * @return 格式化之后的字符串
	 */
	fun formatFileSize(file: File): String {
		return formatFileSize(file, decimalNum = 2)
	}


	/**
	 * 格式化文件大小
	 * @param file 需要格式化的文件
	 * @param decimalNum 要格式化的小数位数
	 * @return 格式化之后的字符串
	 */
	fun formatFileSize(file: File, decimalNum: Int): String {
		return if (!file.exists()) "0B" else formatFileSize(file.length(), decimalNum)
	}


	/**
	 * 格式化文件大小
	 * @param fileSize 需要格式化的文件
	 * @return 格式化之后的字符串
	 */
	fun formatFileSize(fileSize: Long): String {
		return formatFileSize(fileSize, decimalNum = 2)
	}


	/**
	 * 格式化文件大小
	 * @param fileSize 需要格式化的文件大小
	 * @param decimalNum 要格式化的小数位数
	 * @return 格式化之后的字符串
	 */
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
	 * @param inputPath  输入路径
	 * @param outputPath 输出路径
	 * @return 返回码
	 * @see DONE 成功
	 * @see ERROR 失败
	 * @see FILE_NOT_EXIST 文件不存在
	 * @see MAKE_DIR_ERROR 创建文件夹失败
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
			fileInputStream?.close()
			fileOutputStream?.close()
		}
	}

	/**
	 * 将输入流的数据存储到文件中
	 * @param inputStream 输入流
	 * @param file 要存储到的文件
	 * @return 存储结果
	 */
	fun saveFile(inputStream: InputStream?, file: File): Boolean {
		try {
			if (!file.parentFile.exists())
				file.parentFile.mkdirs()
			if (file.exists())
				file.delete()
			val dataInputStream = DataInputStream(BufferedInputStream(inputStream))
			val dataOutputStream = DataOutputStream(BufferedOutputStream(FileOutputStream(file)))
			val bytes = ByteArray(1024 * 1024)
			while (true) {
				val read = dataInputStream.read(bytes)
				if (read <= 0)
					break
				dataOutputStream.write(bytes, 0, read)
			}
			dataOutputStream.close()
			return true
		} catch (e: Exception) {
			e.printStackTrace()
			return false
		}
	}

	/**
	 * 删除文件夹
	 * @param path 要删除的文件夹路径
	 * @return 返回码
	 */
	fun deleteDir(path: String): Int = deleteDir(File(path))

	/**
	 * 删除文件夹
	 * @param dir 要删除的文件夹
	 * @return 返回码
	 * @see DONE 成功
	 * @see FILE_NOT_EXIST 文件不存在
	 */
	fun deleteDir(dir: File): Int {
		if (dir.exists()) {
			if (dir.isDirectory) {
				dir.listFiles().forEach {
					deleteDir(it)
				}
			} else {
				dir.delete()
				return DONE
			}
		}
		return FILE_NOT_EXIST
	}
}
