package vip.mystery0.tools.utils

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.utils.IOUtils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPOutputStream

class TarTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = TarTools()
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
				 suffix: String = "tar.gz",
				 isDeleteExistFile: Boolean = true) {
		val tarGzFile = File(savePath, "$archiveFileName.$suffix")
		FileTools.instance.deleteDir(tarGzFile)
		if (tarGzFile.exists()) {
			if (isDeleteExistFile)
				tarGzFile.delete()
			else
				return
		}
		val tempTarFile = File(savePath, "$archiveFileName.tar")
		FileTools.instance.deleteDir(tempTarFile)
		pack(dir, tempTarFile)
		val fileInputStream = FileInputStream(tempTarFile)
		val gzipOutputStream = GZIPOutputStream(FileOutputStream(tarGzFile))
		IOUtils.copy(fileInputStream, gzipOutputStream)
		IOUtils.closeQuietly(fileInputStream)
		IOUtils.closeQuietly(gzipOutputStream)
		tempTarFile.delete()
	}

	/**
	 *  解压缩 tar.gz
	 *  @param dir 解压缩到的目录
	 *  @param tarGzFile 需要解压缩的归档文件
	 */
	fun decompress(dir: File,
				   tarGzFile: File,
				   isDelete: Boolean = false) {
		if (!tarGzFile.exists())
			return
		if (!dir.isDirectory)
			dir.delete()
		if (!dir.exists() && !dir.mkdirs())
			return
		val tarArchiveInputStream = TarArchiveInputStream(GzipCompressorInputStream(BufferedInputStream(FileInputStream(tarGzFile))))
		var tarArchiveEntry: TarArchiveEntry? = tarArchiveInputStream.nextTarEntry
		while (tarArchiveEntry != null) {
			val tempFile = File(dir, tarArchiveEntry.name)
			if (tarArchiveEntry.isDirectory)
				tempFile.mkdirs()
			else {
				val parent = tempFile.parentFile
				if (!parent.exists()) parent.mkdirs()
				val fileOutputStream = FileOutputStream(tempFile)
				IOUtils.copy(tarArchiveInputStream, fileOutputStream)
				IOUtils.closeQuietly(fileOutputStream)
			}
			tarArchiveEntry = tarArchiveInputStream.nextTarEntry
		}
		IOUtils.closeQuietly(tarArchiveInputStream)
		if (isDelete)
			tarGzFile.delete()
	}

	private fun addFilesToCompression(tarArchiveOutputStream: TarArchiveOutputStream, file: File, dir: String) {
		val tarArchiveEntry = TarArchiveEntry(file, "$dir${File.separator}${file.name}")
		tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry)
		when {
			file.isFile -> {
				tarArchiveEntry.size = file.length()
				val bufferedInputStream = BufferedInputStream(FileInputStream(file))
				IOUtils.copy(bufferedInputStream, tarArchiveOutputStream)
				tarArchiveOutputStream.closeArchiveEntry()
				IOUtils.closeQuietly(bufferedInputStream)
			}
			file.isDirectory -> {
				tarArchiveOutputStream.closeArchiveEntry()
				file.listFiles().forEach {
					addFilesToCompression(tarArchiveOutputStream, it, "$dir${File.separator}${file.name}${File.separator}")
				}
			}
		}
	}

	/**
	 * 将文件集合压缩成tar包后返回
	 *
	 * @param baseDir 要压缩的文件夹
	 * @param target tar 输出流的目标文件
	 * @return File  指定返回的目标文件
	 */
	private fun pack(baseDir: File, target: File): File {
		val tarArchiveOutputStream = TarArchiveOutputStream(FileOutputStream(target))
		tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
		addFilesToCompression(tarArchiveOutputStream, baseDir, "")
		IOUtils.closeQuietly(tarArchiveOutputStream)
		return target
	}
}