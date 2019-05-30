package vip.mystery0.tools.utils

import org.apache.commons.compress.archivers.zip.Zip64Mode
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile
import org.apache.commons.compress.utils.IOUtils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ZipTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = ZipTools()
	}

	/**
	 * 归档并压缩指定目录
	 * @param dir 归档的目录
	 * @param archiveFileName 归档后文件名
	 * @param savePath 归档后文件存放路径
	 * @param suffix 归档后文件的扩展名
	 * @param isDeleteExistFile 是否替换已存在的文件
	 */
	fun compress(dir: File,
				 archiveFileName: String,
				 savePath: File,
				 suffix: String = "zip",
				 isDeleteExistFile: Boolean = true) {
		val zipFile = File(savePath, "$archiveFileName.$suffix")
		FileTools.instance.deleteDir(zipFile)
		if (zipFile.exists()) {
			if (isDeleteExistFile)
				zipFile.delete()
			else
				return
		}
		val zipArchiveOutputStream = ZipArchiveOutputStream(zipFile)
		zipArchiveOutputStream.setUseZip64(Zip64Mode.AsNeeded)
		zipArchiveOutputStream.encoding = "GBK"
		addFilesToCompression(zipArchiveOutputStream, dir, "")
		IOUtils.closeQuietly(zipArchiveOutputStream)
	}

	/**
	 *  解压缩 zip
	 *  @param dir 解压缩到的目录
	 *  @param zipFile 需要解压缩的归档文件
	 */
	fun decompress(dir: File,
				   zipFile: File,
				   isDelete: Boolean = false) {
		if (!zipFile.exists())
			throw RuntimeException("文件不存在！")
		if (!dir.isDirectory)
			dir.delete()
		if (!dir.exists() && !dir.mkdirs())
			throw RuntimeException("目录创建失败！")
		unzipFolder(zipFile, dir)
		if (isDelete)
			zipFile.delete()
	}

	private fun addFilesToCompression(zipArchiveOutputStream: ZipArchiveOutputStream, file: File, dir: String) {
		val zipArchiveEntry = ZipArchiveEntry(file, "$dir${File.separator}${file.name}")
		zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry)
		when {
			file.isFile -> {
				val bufferedInputStream = BufferedInputStream(FileInputStream(file))
				IOUtils.copy(bufferedInputStream, zipArchiveOutputStream)
				zipArchiveOutputStream.closeArchiveEntry()
				IOUtils.closeQuietly(bufferedInputStream)
			}
			file.isDirectory -> {
				zipArchiveOutputStream.closeArchiveEntry()
				file.listFiles().forEach {
					addFilesToCompression(zipArchiveOutputStream, it, "$dir${File.separator}${file.name}${File.separator}")
				}
			}
		}
	}

	private fun unzipFolder(uncompressFile: File, descPathFile: File) {
		val zipFile = ZipFile(uncompressFile, "GBK")
		val entries = zipFile.entries
		while (entries.hasMoreElements()) {
			val zipEntry = entries.nextElement()
			val name = zipEntry.name.replace("\\", "/")
			val currentFile = File(descPathFile, name)
			if (name.endsWith("/")) {
				currentFile.mkdirs()
				continue
			} else
				currentFile.parentFile.mkdirs()
			val fileOutputStream = FileOutputStream(currentFile)
			val inputStream = zipFile.getInputStream(zipEntry)
			IOUtils.copy(inputStream, fileOutputStream)
			IOUtils.closeQuietly(fileOutputStream)
			zipFile.close()
		}
	}
}