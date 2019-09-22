package vip.mystery0.tools.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.zip.Zip64Mode
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.*

private fun require(value: Boolean, lazyMessage: () -> Any) {
	if (!value) {
		val message = lazyMessage()
		throw IOException(message.toString())
	}
}

private fun <T : Any> requireNotNull(value: T?, lazyMessage: () -> Any): T {
	require(value != null, lazyMessage)
	return value!!
}

/**
 * 归档并压缩指定目录
 * @param archiveFileName 归档后文件名
 * @param savePath 归档后文件存放路径
 * @param suffix 归档后文件的扩展名
 * @param isDeleteExistFile 是否替换已存在的文件
 */
suspend fun File.zip(archiveFileName: String? = this.nameWithoutExtension,
					 savePath: File? = this.parentFile,
					 suffix: String = "zip",
					 isDeleteExistFile: Boolean = true): File? {
	val file = requireNotNull(this) { "文件不能为null" }
	val outputFile = File(savePath, "$archiveFileName.$suffix")
	if (outputFile.exists()) {
		if (isDeleteExistFile)
			outputFile.deleteDir()
		else
			return null
	}

	fun <T : File> T.addFilesToCompression(zipArchiveOutputStream: ZipArchiveOutputStream, dir: String) {
		val zipArchiveEntry = ZipArchiveEntry(this, "$dir${File.separator}${name}")
		zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry)
		when {
			isFile -> {
				PairStream(BufferedInputStream(FileInputStream(this)), zipArchiveOutputStream).copy(closeOutput = false)
				zipArchiveOutputStream.closeArchiveEntry()
			}
			isDirectory -> {
				zipArchiveOutputStream.closeArchiveEntry()
				listFiles()?.forEach {
					it.addFilesToCompression(zipArchiveOutputStream, "$dir${File.separator}${name}${File.separator}")
				}
			}
		}
	}

	withContext(Dispatchers.IO) {
		ZipArchiveOutputStream(outputFile).use {
			it.setUseZip64(Zip64Mode.AsNeeded)
			it.encoding = "GBK"
			file.addFilesToCompression(it, "")
		}
	}
	return outputFile
}

/**
 *  解压缩 zip
 *  @param dir 解压缩到的目录
 */
suspend fun <T : File> T?.unZip(dir: File,
								isDelete: Boolean = false) {
	val file = requireNotNull(this) { "文件不能为null" }
	require(file.exists()) { "压缩文件（${file.name}）不存在" }
	if (!dir.isDirectory)
		dir.delete()
	require(dir.exists() || dir.mkdirs()) { "输出目录创建失败" }

	withContext(Dispatchers.IO) {
		val zipFile = ZipFile(this@unZip, "GBK")
		val entries = zipFile.entries
		while (entries.hasMoreElements()) {
			val zipEntry = entries.nextElement()
			val name = zipEntry.name.replace("\\", "/")
			val currentFile = File(dir, name)
			if (name.endsWith("/")) {
				currentFile.mkdirs()
				continue
			} else
				currentFile.parentFile?.mkdirs()
			val fileOutputStream = FileOutputStream(currentFile)
			val inputStream = zipFile.getInputStream(zipEntry)
			PairStream(inputStream, fileOutputStream).copy()
		}
	}
	if (isDelete)
		file.delete()
}