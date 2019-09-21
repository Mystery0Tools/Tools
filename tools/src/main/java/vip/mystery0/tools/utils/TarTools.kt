package vip.mystery0.tools.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.*
import java.util.zip.GZIPOutputStream

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
 * 归档指定目录
 * @param archiveFileName 归档后文件名
 * @param savePath 归档后文件存放路径
 * @param suffix 归档后文件的扩展名
 * @param isDeleteExistFile 是否替换已存在的文件
 */
@Throws(IOException::class)
suspend fun <T : File> T?.tar(archiveFileName: String? = this?.nameWithoutExtension,
							  savePath: File? = this?.parentFile,
							  suffix: String = "tar",
							  isDeleteExistFile: Boolean = true): File? {
	val file = requireNotNull(this) { "文件不能为null" }
	val outputFile = File(savePath, "$archiveFileName.$suffix")
	if (outputFile.exists()) {
		if (isDeleteExistFile)
			outputFile.deleteDir()
		else
			return null
	}

	fun <T : File> T.addFilesToCompression(tarArchiveOutputStream: TarArchiveOutputStream, dir: String) {
		val tarArchiveEntry = TarArchiveEntry(this, "$dir${File.separator}${name}")
		tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry)
		when {
			isFile -> {
				tarArchiveEntry.size = length()
				PairStream(BufferedInputStream(FileInputStream(this)), tarArchiveOutputStream).copy(closeOutput = false)
				tarArchiveOutputStream.closeArchiveEntry()
			}
			isDirectory -> {
				tarArchiveOutputStream.closeArchiveEntry()
				listFiles()?.forEach {
					it.addFilesToCompression(tarArchiveOutputStream, "$dir${File.separator}${name}${File.separator}")
				}
			}
		}
	}

	withContext(Dispatchers.IO) {
		TarArchiveOutputStream(FileOutputStream(outputFile)).use {
			it.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
			file.addFilesToCompression(it, "")
		}
	}
	return outputFile
}

@Throws(IOException::class)
suspend fun <T : File> T?.gz(archiveFileName: String? = this?.nameWithoutExtension,
							 savePath: File? = this?.parentFile,
							 suffix: String = "gz",
							 isDeleteExistFile: Boolean = true): File? {
	val file = requireNotNull(this) { "文件不能为null" }
	val outputFile = File(savePath, "$archiveFileName.$suffix")
	if (outputFile.exists()) {
		if (isDeleteExistFile)
			outputFile.deleteDir()
		else
			return null
	}
	require(file.isFile) { "不是文件，无法压缩" }
	withContext(Dispatchers.IO) {
		Pair(FileInputStream(file), GZIPOutputStream(FileOutputStream(outputFile))).copy()
	}
	return outputFile
}

/**
 * 归档并压缩指定目录
 * @param archiveFileName 归档后文件名
 * @param savePath 归档后文件存放路径
 * @param suffix 归档后文件的扩展名
 * @param isDeleteExistFile 是否替换已存在的文件
 */
@Throws(IOException::class)
suspend fun <T : File> T?.tarGz(archiveFileName: String? = this?.nameWithoutExtension,
								savePath: File? = this?.parentFile,
								suffix: String = "tar.gz",
								isDeleteExistFile: Boolean = true): File? {
	val file = requireNotNull(this) { "文件不能为null" }
	val outputFile = File(savePath, "$archiveFileName.$suffix")
	if (outputFile.exists()) {
		if (isDeleteExistFile)
			outputFile.deleteDir()
		else
			return null
	}
	val tarFile = file.tar(isDeleteExistFile = isDeleteExistFile) ?: return null
	val tarGzFile = tarFile.gz(archiveFileName, savePath, suffix, isDeleteExistFile)
	tarFile.delete()
	return tarGzFile
}

/**
 *  解压缩 tar.gz
 *  @param dir 解压缩到的目录
 */
@Throws(IOException::class)
suspend fun <T : File> T?.unTarGz(dir: File,
								  isDeleteOriginFile: Boolean = false) {
	val file = requireNotNull(this) { "文件不能为null" }
	require(file.exists()) { "压缩文件（${file.name}）不存在" }
	if (!dir.isDirectory)
		dir.delete()
	require(dir.exists() || dir.mkdirs()) { "输出目录创建失败" }
	withContext(Dispatchers.IO) {
		TarArchiveInputStream(GzipCompressorInputStream(BufferedInputStream(FileInputStream(file)))).use {
			var tarArchiveEntry: TarArchiveEntry? = it.nextTarEntry
			while (tarArchiveEntry != null) {
				val tempFile = File(dir, tarArchiveEntry.name)
				if (tarArchiveEntry.isDirectory)
					tempFile.mkdirs()
				else {
					val parent = tempFile.parentFile!!
					if (!parent.exists()) parent.mkdirs()
					val fileOutputStream = FileOutputStream(tempFile)
					PairStream(it, fileOutputStream).copy()
				}
				tarArchiveEntry = it.nextTarEntry
			}
		}
		if (isDeleteOriginFile)
			file.delete()
	}
}