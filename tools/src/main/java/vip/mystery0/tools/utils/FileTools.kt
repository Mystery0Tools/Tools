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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vip.mystery0.tools.context
import java.io.*
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.text.DecimalFormat

private fun require(value: Boolean, lazyMessage: () -> Any) {
	if (!value) {
		val message = lazyMessage()
		throw IOException(message.toString())
	}
}

/**
 * 格式化文件大小
 * @param decimalNum 要格式化的小数位数
 * @return 格式化之后的字符串
 */
fun File.toFormatFileSize(decimalNum: Int = 2): String = if (!exists()) "0B" else length().toFormatFileSize(decimalNum)

/**
 * 格式化文件大小
 * @param decimalNum 要格式化的小数位数
 * @return 格式化之后的字符串
 */
fun Long.toFormatFileSize(decimalNum: Int = 2): String {
	if (this <= 0L)
		return "0B"
	val formatString = StringBuilder()
	formatString.append("#.")
	for (i in 0 until decimalNum)
		formatString.append('0')
	val decimalFormat = DecimalFormat(formatString.toString())
	val fileSizeString: String
	fileSizeString = when {
		this < 1024 -> decimalFormat.format(this) + 'B'
		this < 1024 * 1024 -> decimalFormat.format(this.toFloat() / 1024f) + "KB"
		this < 1024 * 1024 * 1024 -> decimalFormat.format(this.toFloat() / 1024f / 1024f) + "MB"
		else -> decimalFormat.format(this.toFloat() / 1024f / 1024f / 1024f) + "GB"
	}
	return fileSizeString
}

/**
 * 拷贝目录的所有内容
 * @param inputPath 输入路径
 * @param outputPath 输出路径
 */
fun copyToDir(inputPath: String, outputPath: String) {
	val inputDir = File(inputPath)
	val outputDir = File(outputPath)
	inputDir.copyToDir(outputDir)
}

/**
 * 拷贝文件
 * @param inputPath  输入路径
 * @param outputPath 输出路径
 */
fun copyToFile(inputPath: String, outputPath: String) {
	val inputFile = File(inputPath)
	val outputFile = File(outputPath)
	inputFile.copyToFile(outputFile)
}

/**
 * 拷贝文件
 * @param outputFile 输出路径
 */
@Throws(IOException::class)
fun File.copyToFile(outputFile: File, ignoreException: Boolean = false) {
	if (!this.exists())
		if (ignoreException) return
		else require(false) { "源文件不存在" }
	if (!this.isFile)
		if (ignoreException) return
		else require(false) { "该项不是文件：${name}(${absolutePath})" }
	if (outputFile.parentFile == null)
		require(false) { "输出目录创建失败！" }
	if (!outputFile.parentFile!!.exists() && !outputFile.parentFile!!.mkdirs())
		if (ignoreException) return
		else require(false) { "输出目录创建失败" }
	FileInputStream(this).useToBoolean {
		val fileOutputStream = FileOutputStream(outputFile)
		it.copy(fileOutputStream)
		fileOutputStream.closeQuietly(true)
		Unit
	}
}

/**
 * 拷贝目录的所有内容
 * @param outputDir 输出路径
 */
@Throws(IOException::class)
fun File.copyToDir(outputDir: File, ignoreException: Boolean = false) {
	if (!exists())
		if (ignoreException) return
		else require(false) { "源目录不存在" }
	if (!isDirectory)
		if (ignoreException) return
		else require(false) { "该项不是目录：${name}(${absolutePath})" }
	if (!outputDir.exists() && !outputDir.mkdirs())
		if (ignoreException) return
		else require(false) { "输出目录创建失败！" }
	listFiles()?.forEach {
		val outputFile = File(outputDir, it.name)
		when {
			it.isFile -> it.copyToFile(outputFile)
			it.isDirectory -> it.copyToDir(outputFile)
		}
	}
}

/**
 * 删除文件夹
 * @param path 要删除的文件夹路径
 * @param isDeleteDir 是否删除目录
 * @param ignoreException 是否忽略异常
 * @return 返回码
 */
suspend fun deleteDir(path: String,
					  isDeleteDir: Boolean = true,
					  ignoreException: Boolean = true) = File(path).deleteDir(isDeleteDir, ignoreException)

/**
 * 删除文件夹
 * @param isDeleteDir 是否删除目录
 * @param ignoreException 是否忽略异常
 * @return 返回码
 */
suspend fun File.deleteDir(isDeleteDir: Boolean = true,
						   ignoreException: Boolean = true) {
	if (exists()) {
		withContext(Dispatchers.IO) {
			if (isDirectory) {
				listFiles()?.forEach {
					it.deleteDir()
				}
				if (isDeleteDir)
					delete()
				Unit
			} else
				delete()
		}
	} else
		if (!ignoreException)
			require(false) { "文件不存在：${name}(${absolutePath})" }
}

/**
 * 将Uri的内容克隆到临时文件，然后返回临时文件
 * @param file 临时文件
 */
@Throws(IOException::class)
suspend fun Uri.cloneToFile(file: File) = context().contentResolver.openInputStream(this).copyToFile(file)

/**
 * 将输入流的数据存储到文件中
 * @param file 要存储到的文件
 * @param closeInput 是否自动关闭输入流
 * @return 存储结果
 */
@Throws(IOException::class)
suspend fun <T : InputStream> T?.copyToFile(file: File,
											closeInput: Boolean = true): Boolean {
	require(this != null) { "输入流不能为空" }
	require(file.parentFile != null) { "输出目录创建失败！" }
	return withContext(Dispatchers.IO) {
		if (!file.parentFile!!.exists())
			file.parentFile?.mkdirs()
		if (file.exists())
			file.delete()
		val result = FileOutputStream(file).useToBoolean {
			this@copyToFile.copy(it)
		}
		if (closeInput)
			closeQuietly(true)
		result
	}
}

/**
 * 将文件的内容写入到输出流中
 * @param outputStream
 * @param closeOutput 是否自动关闭流
 * @return 拷贝结果
 */
@Throws(IOException::class)
suspend fun File.copyToStream(outputStream: OutputStream?,
							  closeOutput: Boolean = true): Boolean {
	require(exists()) { "文件不存在" }
	return withContext(Dispatchers.IO) {
		FileInputStream(this@copyToStream).useToBoolean {
			it.copy(outputStream)
			if (closeOutput)
				it.closeQuietly(true)
			Unit
		}
	}
}

/**
 * bitmap转成byte数组
 * @param compressFormat 需要转换的格式
 * @return 转换后的byte数组
 */
fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat): ByteArray {
	val byteArrayOutputStream = ByteArrayOutputStream()
	compress(compressFormat, 100, byteArrayOutputStream)
	val result = byteArrayOutputStream.toByteArray()
	byteArrayOutputStream.close()
	return result
}

/**
 * 压缩图片并进行base64加密
 * @param compressFormat 压缩的格式
 * @param maxSize 压缩之后最大的大小
 * @param interval 每次压缩的压缩率差值
 * @return 字节数组
 */
@Throws(IOException::class)
suspend fun File.composeImage(compressFormat: Bitmap.CompressFormat, maxSize: Int, interval: Int): ByteArray {
	require(interval in 0..100) { "interval can not be less 0 or more than 100" }
	return withContext(Dispatchers.IO) {
		val outputStream = ByteArrayOutputStream()
		var option = 100
		var base64String: String
		while (option <= 0) {
			outputStream.reset()
			val bitmap = BitmapFactory.decodeFile(absolutePath)
			bitmap.compress(compressFormat, option, outputStream)
			base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
			if (base64String.length <= maxSize)
				break
			option -= interval
		}
		val result = outputStream.toByteArray()
		outputStream.close()
		result
	}
}

fun File.md5(): String {
	val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
	val messageDigest = MessageDigest.getInstance("MD5")
	messageDigest.update(FileInputStream(this).channel.map(FileChannel.MapMode.READ_ONLY, 0, length()))
	val bytes = messageDigest.digest()
	val stringBuffer = StringBuffer(2 * bytes.size)
	for (byte in bytes) {
		val c0 = hexDigits[byte.toInt() and 0xf0 shr 4]
		val c1 = hexDigits[byte.toInt() and 0xf]
		stringBuffer.append(c0).append(c1)
	}
	return stringBuffer.toString()
}