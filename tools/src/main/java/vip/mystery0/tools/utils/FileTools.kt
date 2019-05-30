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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.*
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.text.DecimalFormat

class FileTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	class FileToolsException(val code: Int, val msg: String?) : RuntimeException() {
		companion object {
			const val ERROR = 101
			const val FILE_NOT_EXIST = 102
			const val MAKE_DIR_ERROR = 103
		}

		constructor(e: Exception) : this(ERROR, e.message)
		constructor() : this(ERROR, null)
	}

	private object Holder {
		val holder = FileTools()
	}

	/**
	 * 从uri中获取路径
	 * @param context 上下文
	 * @param uri 需要获取路径的uri对象
	 * @return 提取到的路径
	 *
	 * @Deprecated 1.7.2
	 * @see cloneUriToFile(Context,Uri,File)
	 */
	@Deprecated("事实上，在不同设备上，这个方法很容易出问题，比如说不适配第三方图库，所以在1.7.2中标记为过时，请使用cloneUriToFile方法将Uri中的内容存储到临时文件中")
	fun getPath(context: Context, uri: Uri): String? {
		throw Exception("该方法已删除，请使用 cloneUriToFile")
	}

	/**
	 * 将Uri的内容克隆到临时文件，然后返回临时文件
	 *
	 * @param context 上下文
	 * @param uri 选择器返回的Uri
	 * @param file 临时文件
	 */
	fun cloneUriToFile(context: Context, uri: Uri, file: File) {
		saveFile(context.contentResolver.openInputStream(uri), file)
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
	 * @param decimalNum 要格式化的小数位数
	 * @return 格式化之后的字符串
	 */
	fun formatFileSize(file: File, decimalNum: Int = 2): String {
		return if (!file.exists()) "0B" else formatFileSize(file.length(), decimalNum)
	}

	/**
	 * 格式化文件大小
	 * @param fileSize 需要格式化的文件大小
	 * @param decimalNum 要格式化的小数位数
	 * @return 格式化之后的字符串
	 */
	fun formatFileSize(fileSize: Long, decimalNum: Int = 2): String {
		if (fileSize == 0L)
			return "0B"
		val formatString = StringBuilder()
		formatString.append("#.")
		for (i in 0 until decimalNum)
			formatString.append('0')
		val decimalFormat = DecimalFormat(formatString.toString())
		val fileSizeString: String
		fileSizeString = when {
			fileSize < 1024 -> decimalFormat.format(fileSize) + 'B'
			fileSize < 1024 * 1024 -> decimalFormat.format(fileSize.toFloat() / 1024f) + "KB"
			fileSize < 1024 * 1024 * 1024 -> decimalFormat.format(fileSize.toFloat() / 1024f / 1024f) + "MB"
			else -> decimalFormat.format(fileSize.toFloat() / 1024f / 1024f / 1024f) + "GB"
		}
		return fileSizeString
	}

	/**
	 * 拷贝目录的所有内容
	 * @param inputPath 输入路径
	 * @param outputPath 输出路径
	 * @return 返回码
	 */
	fun copyDir(inputPath: String, outputPath: String) {
		val inputFile = File(inputPath)
		val outputFile = File(outputPath)
		if (!inputFile.exists())
			throw FileToolsException(FileToolsException.FILE_NOT_EXIST, "源文件不存在！")
		if (!outputFile.exists() && !outputFile.mkdirs())
			throw FileToolsException(FileToolsException.MAKE_DIR_ERROR, "输出目录创建失败！")
		try {
			inputFile.listFiles()
					.forEach {
						val pathInDir = it.absolutePath.substring(inputPath.length)
						when {
							it.isFile -> copyFile(it.absolutePath, "$outputPath$pathInDir")
							it.isDirectory -> copyDir(it.absolutePath, "$outputPath$pathInDir")
						}
					}
		} catch (e: Exception) {
			e.printStackTrace()
			throw FileToolsException(e)
		}
	}

	/**
	 * 拷贝文件
	 * @param inputPath  输入路径
	 * @param outputPath 输出路径
	 * @return 返回码
	 */
	fun copyFile(inputPath: String, outputPath: String) {
		if (!File(inputPath).exists()) {
			throw FileToolsException(FileToolsException.FILE_NOT_EXIST, "源文件不存在！")
		}
		val outputParentFile = File(outputPath).parentFile
		if (!outputParentFile.exists() && !outputParentFile.mkdirs())
			throw FileToolsException(FileToolsException.MAKE_DIR_ERROR, "输出目录创建失败！")
		var fileInputStream: FileInputStream? = null
		var fileOutputStream: FileOutputStream? = null
		try {
			fileInputStream = FileInputStream(inputPath)
			fileOutputStream = FileOutputStream(outputPath)
			val bytes = ByteArray(1024 * 1024)
			var readCount = fileInputStream.read(bytes)
			while (readCount != -1) {
				fileOutputStream.write(bytes, 0, readCount)
				readCount = fileInputStream.read(bytes)
			}
		} catch (e: Exception) {
			e.printStackTrace()
			throw FileToolsException(e)
		} finally {
			fileInputStream?.close()
			fileOutputStream?.close()
		}
	}

	/**
	 * 将文件的内容写入到输出流中
	 * @param inputFile
	 * @param fileOutputStream
	 * @param closeStreamFinally 是否自动关闭流
	 * @return 拷贝结果
	 */
	fun copyFileToOutputStream(inputFile: File, fileOutputStream: OutputStream?, closeStreamFinally: Boolean = true): Boolean {
		var fileInputStream: FileInputStream? = null
		try {
			fileInputStream = FileInputStream(inputFile)
			val bytes = ByteArray(1024 * 1024)
			var readCount = fileInputStream.read(bytes)
			while (readCount != -1) {
				fileOutputStream?.write(bytes, 0, readCount)
				readCount = fileInputStream.read(bytes)
			}
			return true
		} catch (e: Exception) {
			e.printStackTrace()
			return false
		} finally {
			fileInputStream?.close()
			if (closeStreamFinally)
				fileOutputStream?.close()
		}
	}

	/**
	 * 将输入流的数据存储到文件中
	 * @param inputStream 输入流
	 * @param file 要存储到的文件
	 * @param closeStreamFinally 是否自动关闭流
	 * @return 存储结果
	 */
	fun saveFile(inputStream: InputStream?, file: File, closeStreamFinally: Boolean = true): Boolean {
		var outputStream: OutputStream? = null
		try {
			if (!file.parentFile.exists())
				file.parentFile.mkdirs()
			if (file.exists())
				file.delete()
			val dataInputStream = DataInputStream(BufferedInputStream(inputStream))
			outputStream = DataOutputStream(BufferedOutputStream(FileOutputStream(file)))
			val bytes = ByteArray(1024 * 1024)
			while (true) {
				val read = dataInputStream.read(bytes)
				if (read <= 0)
					break
				outputStream.write(bytes, 0, read)
			}
			return true
		} catch (e: Exception) {
			e.printStackTrace()
			return false
		} finally {
			if (closeStreamFinally)
				inputStream?.close()
			outputStream?.close()
		}
	}

	/**
	 * 删除文件夹
	 * @param path 要删除的文件夹路径
	 * @return 返回码
	 */
	fun deleteDir(path: String, isDeleteDir: Boolean = true) = deleteDir(File(path), isDeleteDir)

	/**
	 * 删除文件夹
	 * @param dir 要删除的文件夹
	 * @return 返回码
	 * @see DONE 成功
	 * @see FILE_NOT_EXIST 文件不存在
	 */
	fun deleteDir(dir: File, isDeleteDir: Boolean = true) {
		if (dir.exists()) {
			if (dir.isDirectory) {
				dir.listFiles().forEach {
					deleteDir(it)
				}
				if (isDeleteDir)
					dir.delete()
			} else
				dir.delete()
		} else
			throw FileToolsException(FileToolsException.FILE_NOT_EXIST, "文件不存在：${dir.name}(${dir.absolutePath})")
	}

	fun getMD5(file: File): String {
		var md5 = ""
		try {
			val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
			val messageDigest = MessageDigest.getInstance("MD5")
			messageDigest.update(FileInputStream(file).channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length()))
			val bytes = messageDigest.digest()
			val stringBuffer = StringBuffer(2 * bytes.size)
			for (byte in bytes) {
				val c0 = hexDigits[byte.toInt() and 0xf0 shr 4]
				val c1 = hexDigits[byte.toInt() and 0xf]
				stringBuffer.append(c0).append(c1)
			}
			md5 = stringBuffer.toString()
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return md5
	}

	fun <T> write(filePath: String, data: T) {
		val file = File(filePath)
		if (file.exists()) file.delete()
		if (!file.parentFile.exists()) file.parentFile.mkdirs()
		var objectOutputStream: ObjectOutputStream? = null
		try {
			objectOutputStream = ObjectOutputStream(FileOutputStream(file))
			objectOutputStream.writeObject(data)
		} catch (ignore: Exception) {
		} finally {
			objectOutputStream?.close()
		}
	}

	fun <T> read(filePath: String): T? {
		val file = File(filePath)
		if (!file.exists()) return null
		var objectInputStream: ObjectInputStream? = null
		var data: T? = null
		try {
			objectInputStream = ObjectInputStream(FileInputStream(file))
			@Suppress("UNCHECKED_CAST")
			data = objectInputStream.readObject() as T
		} catch (ignore: Exception) {
		} finally {
			objectInputStream?.close()
		}
		return data
	}
}
