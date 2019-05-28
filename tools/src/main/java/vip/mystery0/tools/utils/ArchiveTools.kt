package vip.mystery0.tools.utils

import java.io.File

class ArchiveTools private constructor(private val type: Int) {
	private lateinit var archiveFile: File//压缩文件
	private lateinit var compressDir: File//压缩的目录
	private lateinit var decompressDir: File//解压缩的目录
	private lateinit var archiveFileName: String//压缩文件的文件名
	private lateinit var suffix: String//扩展名
	private lateinit var savePath: File//压缩文件存储路径
	private var isDeleteExistFile: Boolean = true//是否替换已存在的文件，指的是压缩文件
	private var isDelete: Boolean = false//解压后是否自动删除压缩文件

	companion object {
		const val TYPE_TAR_GZ = 1
		const val TYPE_ZIP = 2

		fun create(type: Int): ArchiveTools = ArchiveTools(type)

		fun tarGz(): ArchiveTools = ArchiveTools(TYPE_TAR_GZ)

		fun zip(): ArchiveTools = ArchiveTools(TYPE_ZIP)
	}

	fun compress() {
		when (type) {
			TYPE_TAR_GZ -> TarTools.instance.compress(compressDir, archiveFileName, savePath, suffix, isDeleteExistFile)
			TYPE_ZIP -> ZipTools.instance.compress(compressDir, archiveFileName, savePath, suffix, isDeleteExistFile)
			else -> throw NoSuchElementException("类型错误")
		}
	}

	fun decompress() {
		when (type) {
			TYPE_TAR_GZ -> TarTools.instance.decompress(decompressDir, archiveFile, isDelete)
			TYPE_ZIP -> ZipTools.instance.decompress(decompressDir, archiveFile, isDelete)
			else -> throw NoSuchElementException("类型错误")
		}
	}

	/**
	 * 设置压缩文件
	 */
	fun fromFile(archiveFile: File): ArchiveTools {
		this.archiveFile = archiveFile
		return this
	}

	/**
	 * 设置压缩的目录
	 */
	fun setCompressDir(dir: File): ArchiveTools {
		this.compressDir = dir
		return this
	}

	/**
	 * 设置解压缩的目录
	 */
	fun setDecompressDir(dir: File): ArchiveTools {
		this.decompressDir = dir
		return this
	}

	/**
	 * 设置压缩文件的名称
	 */
	fun setArchiveFileName(archiveFileName: String): ArchiveTools {
		this.archiveFileName = archiveFileName
		return this
	}

	/**
	 * 设置压缩文件的存储路径，压缩文件的父级目录
	 */
	fun setSavePath(savePath: File): ArchiveTools {
		this.savePath = savePath
		return this
	}

	/**
	 * 设置压缩文件扩展名
	 */
	fun setSuffix(suffix: String): ArchiveTools {
		this.suffix = suffix
		return this
	}

	/**
	 * 设置压缩文件的存储路径，压缩文件本身
	 */
	fun saveTo(saveFile: File): ArchiveTools {
		this.decompressDir = saveFile
		this.archiveFileName = saveFile.nameWithoutExtension
		this.savePath = saveFile.parentFile
		this.suffix = saveFile.extension
		return this
	}

	/**
	 * 如果已存在压缩文件，则替代
	 */
	fun replaceExist(): ArchiveTools = replaceExist(true)

	fun replaceExist(isDeleteExistFile: Boolean): ArchiveTools {
		this.isDeleteExistFile = isDeleteExistFile
		return this
	}

	/**
	 * 解压缩之后自动删除压缩文件
	 */
	fun deleteAfterDecompress(): ArchiveTools = deleteAfterDecompress(true)

	fun deleteAfterDecompress(isDelete: Boolean): ArchiveTools {
		this.isDelete = isDelete
		return this
	}
}