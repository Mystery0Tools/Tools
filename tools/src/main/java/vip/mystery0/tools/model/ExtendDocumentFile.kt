package vip.mystery0.tools.model

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import vip.mystery0.tools.utils.PackageTools
import vip.mystery0.tools.utils.SAFFileTools.getChildFile
import vip.mystery0.tools.utils.SAFFileTools.getChildDirectory
import vip.mystery0.tools.utils.SAFFileTools.mkdirs
import java.io.*
import java.net.URI
import java.net.URL
import java.nio.file.Path

class ExtendDocumentFile : File {
	private lateinit var documentFile: DocumentFile
	val uri by lazy { documentFile.uri }

	constructor(pathName: String) : super(pathName) {
		throw UnsupportedOperationException("ExtendDocumentFile 不支持此种构造函数，请使用Factory")
	}

	constructor(parent: String, child: String) : super(parent, child) {
		throw UnsupportedOperationException("ExtendDocumentFile 不支持此种构造函数，请使用Factory")
	}

	constructor(parent: File, child: String) : super(parent, child) {
		throw UnsupportedOperationException("ExtendDocumentFile 不支持此种构造函数，请使用Factory")
	}

	constructor(uri: URI) : super(uri) {
		throw UnsupportedOperationException("ExtendDocumentFile 不支持此种构造函数，请使用Factory")
	}

	constructor(documentFile: DocumentFile) : super("") {
		this.documentFile = documentFile
	}

	fun getChildFile(fileName: String, deleteWhenNotFile: Boolean = true): ExtendDocumentFile? {
		val child = documentFile.getChildFile(fileName, deleteWhenNotFile) ?: return null
		return ExtendDocumentFile(child)
	}

	fun getChildDirectory(dirName: String, deleteWhenNotDirectory: Boolean = true): ExtendDocumentFile? {
		val dir = documentFile.getChildDirectory(dirName, deleteWhenNotDirectory) ?: return null
		return ExtendDocumentFile(dir)
	}

	fun mkdirs(rootTreeUri: Uri): Boolean = documentFile.mkdirs(Factory.context, rootTreeUri)

	fun renameTo(dest: ExtendDocumentFile?): Boolean {
		if (dest == null)
			return false
		var fileInputStream: FileInputStream? = null
		var fileOutputStream: FileOutputStream? = null
		try {
			fileInputStream = FileInputStream(Factory.context.contentResolver.openFileDescriptor(dest.uri, "r")?.fileDescriptor)
			fileOutputStream = FileOutputStream(Factory.context.contentResolver.openFileDescriptor(dest.uri, "w")?.fileDescriptor)
			val bytes = ByteArray(1024 * 1024 * 10)
			var readCount = 0
			while (readCount != -1) {
				fileOutputStream.write(bytes, 0, readCount)
				readCount = fileInputStream.read(bytes)
			}
			delete()
			return true
		} catch (e: Exception) {
			e.printStackTrace()
			return false
		} finally {
			fileInputStream?.close()
			fileOutputStream?.close()
		}
	}

	override fun getName(): String? = documentFile.name

	override fun getPath(): String? = documentFile.uri.path

	override fun getParentFile(): ExtendDocumentFile? {
		val parent = documentFile.parentFile ?: return null
		return ExtendDocumentFile(parent)
	}

	override fun getParent(): String? = parentFile?.path

	override fun isAbsolute(): Boolean = documentFile.uri.isAbsolute

	override fun getAbsolutePath(): String? = documentFile.uri.path

	@Deprecated("not implement", ReplaceWith("null"))
	override fun getAbsoluteFile(): ExtendDocumentFile? = null

	override fun deleteOnExit() {
		delete()
	}

	override fun list(): Array<String?> = listFiles().map { it.name }.toTypedArray()

	override fun list(filter: FilenameFilter?): Array<String?> = listFiles(filter).map { it.name }.toTypedArray()

	override fun canRead(): Boolean = documentFile.canRead()

	override fun mkdir(): Boolean {
		throw UnsupportedOperationException("该方法需要一个根目录的TreeUri，请转用 SAFFileTools.mkdirs 方法")
	}

	override fun mkdirs(): Boolean {
		throw UnsupportedOperationException("该方法需要一个根目录的TreeUri，请转用 SAFFileTools.mkdirs 方法")
	}

	override fun isFile(): Boolean = documentFile.isFile

	override fun getCanonicalPath(): String {
		throw UnsupportedOperationException("不知道这个方法是什么用处……")
	}

	override fun getCanonicalFile(): File {
		throw UnsupportedOperationException("不知道这个方法是什么用处……")
	}

	override fun createNewFile(): Boolean {
		throw UnsupportedOperationException("当该对象不为空时，一般来说该文件不为空！")
	}

	override fun toPath(): Path {
		throw UnsupportedOperationException("不知道这个方法是什么用处……")
	}

	override fun canWrite(): Boolean = documentFile.canWrite()

	override fun listFiles(): Array<ExtendDocumentFile> = documentFile.listFiles().map { ExtendDocumentFile(it) }.toTypedArray()

	override fun listFiles(filter: FilenameFilter?): Array<ExtendDocumentFile> {
		val files = documentFile.listFiles()
		if (filter != null)
			return files.filter { filter.accept(parentFile, name) }.map { ExtendDocumentFile(it) }
					.toTypedArray()
		return files.map { ExtendDocumentFile(it) }.toTypedArray()
	}

	override fun listFiles(filter: FileFilter?): Array<ExtendDocumentFile> {
		val files = documentFile.listFiles()
		if (filter != null)
			return files.filter { filter.accept(ExtendDocumentFile(it)) }
					.map { ExtendDocumentFile(it) }
					.toTypedArray()
		return files.map { ExtendDocumentFile(it) }.toTypedArray()
	}

	override fun setExecutable(executable: Boolean, ownerOnly: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setExecutable(executable: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setWritable(writable: Boolean, ownerOnly: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setWritable(writable: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setReadOnly(): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setReadable(readable: Boolean, ownerOnly: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun setReadable(readable: Boolean): Boolean {
		throw UnsupportedOperationException("权限在读取的时候已经决定，似乎无法修改……")
	}

	override fun canExecute(): Boolean = true

	override fun getTotalSpace(): Long {
		throw UnsupportedOperationException("不支持的方法")
	}

	override fun getFreeSpace(): Long {
		throw UnsupportedOperationException("不支持的方法")
	}

	override fun getUsableSpace(): Long {
		throw UnsupportedOperationException("不支持的方法")
	}

	override fun toURI(): URI = URI(documentFile.uri.toString())

	override fun isHidden(): Boolean = name?.startsWith('.') ?: false

	override fun lastModified(): Long = documentFile.lastModified()

	override fun delete(): Boolean = documentFile.delete()

	override fun length(): Long = documentFile.length()

	override fun setLastModified(time: Long): Boolean {
		throw UnsupportedOperationException("不支持的方法")
	}

	override fun isDirectory(): Boolean = documentFile.isDirectory

	override fun renameTo(dest: File?): Boolean {
		throw UnsupportedOperationException("不支持的方法，请使用重载方法")
	}

	override fun toURL(): URL = URL(documentFile.uri.toString())

	override fun exists(): Boolean = documentFile.exists()

	@SuppressLint("StaticFieldLeak")
	object Factory {
		lateinit var context: Context
			private set

		@SuppressLint("NewApi")
		fun createByTreeUri(context: Context, uri: Uri): ExtendDocumentFile? {
			if (PackageTools.isAfter(PackageTools.VERSION_N) && !DocumentsContract.isTreeUri(uri))
				return null
			return createByDocumentFile(context, DocumentFile.fromTreeUri(context, uri))
		}

		fun createBySingleUri(context: Context, uri: Uri): ExtendDocumentFile? = createByDocumentFile(context, DocumentFile.fromSingleUri(context, uri))

		fun createByDocumentFile(context: Context, file: DocumentFile?): ExtendDocumentFile? {
			this.context = context.applicationContext
			if (file == null) return null
			return ExtendDocumentFile(file)
		}
	}
}