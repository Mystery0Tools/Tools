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
import org.apache.commons.compress.utils.IOUtils
import vip.mystery0.tools.ToolsException
import vip.mystery0.tools.tryOrBoolean
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

	private object Holder {
		val holder = FileTools()
	}

	/**
	 * 将Uri的内容克隆到临时文件，然后返回临时文件
	 *
	 * @param context 上下文
	 * @param uri 选择器返回的Uri
	 * @param file 临时文件
	 */
	fun cloneUriToFile(context: Context, uri: Uri, file: File) {
		copyInputStreamToFile(context.contentResolver.openInputStream(uri), file)
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
	 * @param inputDir 输入路径
	 * @param outputDir 输出路径
	 */
	fun copyDir(inputDir: File, outputDir: File, ignoreException: Boolean = false) {
		if (!inputDir.exists())
			if (ignoreException) return
			else throw ToolsException(ToolsException.FILE_NOT_EXIST, "源目录不存在！")
		if (!inputDir.isDirectory)
			if (ignoreException) return
			else throw ToolsException(ToolsException.NOT_DIRECTORY, "该项不是目录：${inputDir.name}(${inputDir.absolutePath})")
		if (!outputDir.exists() && !outputDir.mkdirs())
			if (ignoreException) return
			else throw ToolsException(ToolsException.MAKE_DIR_ERROR, "输出目录创建失败！")
		inputDir.listFiles()
				.forEach {
					val outputFile = File(outputDir, it.name)
					when {
						it.isFile -> copyFile(it, outputFile)
						it.isDirectory -> copyDir(it, outputFile)
					}
				}
	}

	/**
	 * 拷贝目录的所有内容
	 * @param inputPath 输入路径
	 * @param outputPath 输出路径
	 */
	fun copyDir(inputPath: String, outputPath: String) {
		val inputDir = File(inputPath)
		val outputDir = File(outputPath)
		copyDir(inputDir, outputDir)
	}

	/**
	 * 拷贝文件
	 * @param inputFile  输入路径
	 * @param outputFile 输出路径
	 */
	fun copyFile(inputFile: File, outputFile: File, ignoreException: Boolean = false) {
		if (!inputFile.exists())
			if (ignoreException) return
			else throw ToolsException(ToolsException.FILE_NOT_EXIST, "源文件不存在！")
		if (!inputFile.isFile)
			if (ignoreException) return
			else throw ToolsException(ToolsException.NOT_FILE, "该项不是文件：${inputFile.name}(${inputFile.absolutePath})")
		if (!outputFile.parentFile.exists() && !outputFile.parentFile.mkdirs())
			if (ignoreException) return
			else throw ToolsException(ToolsException.MAKE_DIR_ERROR, "输出目录创建失败！")
		var fileInputStream: FileInputStream? = null
		var fileOutputStream: FileOutputStream? = null
		try {
			fileInputStream = FileInputStream(inputFile)
			fileOutputStream = FileOutputStream(outputFile)
			IOUtils.copy(fileInputStream, fileOutputStream)
		} catch (e: IOException) {
			e.printStackTrace()
			throw ToolsException(e)
		} finally {
			IOUtils.closeQuietly(fileInputStream)
			IOUtils.closeQuietly(fileOutputStream)
		}
	}

	/**
	 * 拷贝文件
	 * @param inputPath  输入路径
	 * @param outputPath 输出路径
	 */
	fun copyFile(inputPath: String, outputPath: String) {
		val inputFile = File(inputPath)
		val outputFile = File(outputPath)
		copyFile(inputFile, outputFile)
	}

	/**
	 * 将文件的内容写入到输出流中
	 * @param inputFile
	 * @param outputStream
	 * @param closeStreamFinally 是否自动关闭流
	 * @return 拷贝结果
	 */
	fun copyFileToOutputStream(inputFile: File, outputStream: OutputStream?, closeStreamFinally: Boolean = true): Boolean {
		var fileInputStream: FileInputStream? = null
		return tryOrBoolean(trys = {
			fileInputStream = FileInputStream(inputFile)
			IOUtils.copy(fileInputStream, outputStream)
		}, finally = {
			IOUtils.closeQuietly(fileInputStream)
			if (closeStreamFinally)
				IOUtils.closeQuietly(outputStream)
		})
	}

	/**
	 * 将输入流的数据存储到文件中
	 * @param inputStream 输入流
	 * @param outputFile 要存储到的文件
	 * @param closeStreamFinally 是否自动关闭流
	 * @return 存储结果
	 */
	fun copyInputStreamToFile(inputStream: InputStream?, outputFile: File, closeStreamFinally: Boolean = true): Boolean {
		if (!outputFile.parentFile.exists())
			outputFile.parentFile.mkdirs()
		if (outputFile.exists())
			outputFile.delete()
		var fileOutputStream: FileOutputStream? = null
		return tryOrBoolean(trys = {
			fileOutputStream = FileOutputStream(outputFile)
			IOUtils.copy(inputStream, fileOutputStream)
		}, finally = {
			if (closeStreamFinally)
				IOUtils.closeQuietly(inputStream)
			IOUtils.closeQuietly(fileOutputStream)
		})
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
	 */
	fun deleteDir(dir: File, isDeleteDir: Boolean = true, ignoreException: Boolean = true) {
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
			if (!ignoreException)
				throw ToolsException(ToolsException.FILE_NOT_EXIST, "文件不存在：${dir.name}(${dir.absolutePath})")
	}

	fun getMD5(file: File): String {
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
		return stringBuffer.toString()
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
