package vip.mystery0.tools.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import vip.mystery0.tools.factory.MimeTypeFactory

class SAFFileTools private constructor() {
	companion object {
		val INSTANCE by lazy { Holder.holder }
		val instance = INSTANCE
	}

	private object Holder {
		val holder = SAFFileTools()
	}

	/**
	 * 从当前路径获取子文件，如果不存在就创建
	 * @param fileName 子文件名称
	 * @param deleteWhenNotFile 当子文件存在但是不是文件时是否需要删除重新创建
	 * @return 子文件或者创建失败为空
	 */
	fun DocumentFile?.getChildFile(fileName: String, deleteWhenNotFile: Boolean = true): DocumentFile? {
		if (this == null || !this.exists())
			return null
		var file: DocumentFile? = this.findFile(fileName)
		if (file == null || !file.exists())
			return this.createFile(MimeTypeFactory.typeOfFileName(fileName), fileName)
		if (!file.isFile && deleteWhenNotFile) {
			file.delete()
			file = this.createFile(MimeTypeFactory.typeOfFileName(fileName), fileName)
		}
		return file
	}

	/**
	 * 从当前路径获取子目录，如果不存在就创建
	 * @param dirName 子目录名称
	 * @param deleteWhenNotDirectory 当子目录存在但是不是目录时是否需要删除重新创建
	 * @return 子目录或者创建失败为空
	 */
	fun DocumentFile?.getChildDirectory(dirName: String, deleteWhenNotDirectory: Boolean = true): DocumentFile? {
		if (this == null || !this.exists())
			return null
		var dir = this.findFile(dirName)
		if (dir == null || !dir.exists())
			return this.createDirectory(dirName)
		if (!dir.isDirectory && deleteWhenNotDirectory) {
			dir.delete()
			dir = this.createDirectory(dirName)
		}
		return dir
	}

	/**
	 * 递归创建目录，使用DocumentFile分析从根目录到当前目录的层级关系，然后调用重载方法递归创建目录
	 * @param context 上下文
	 * @param rootTreeUri 根目录的TreeUri
	 * @return 创建结果
	 */
	@SuppressLint("NewApi")
	fun DocumentFile?.mkdirs(context: Context, rootTreeUri: Uri): Boolean {
		if (PackageTools.instance.isAfter(PackageTools.VERSION_N) && !DocumentsContract.isTreeUri(rootTreeUri))
			return false
		fun DocumentFile?.checkParentUri(rootTreeUri: Uri, list: ArrayList<String>) {
			if (this == null)
				return
			if (this.uri != rootTreeUri)
				this.parentFile.checkParentUri(rootTreeUri, list)
			list.add(this.name!!)
		}

		val list = ArrayList<String>()
		this.checkParentUri(rootTreeUri, list)
		return mkdirs(context, rootTreeUri, list)
	}

	/**
	 * 根据层级关系递归创建目录
	 * @param context 上下文
	 * @param rootTreeUri 根目录的TreeUri
	 * @param levelList 层级关系
	 * @return 创建结果
	 */
	@SuppressLint("NewApi")
	fun mkdirs(context: Context, rootTreeUri: Uri, levelList: List<String>): Boolean {
		if (PackageTools.instance.isAfter(PackageTools.VERSION_N) && !DocumentsContract.isTreeUri(rootTreeUri))
			return false
		var parent = DocumentFile.fromTreeUri(context, rootTreeUri)
		levelList.forEach {
			parent = parent.getChildDirectory(it)
			if (parent == null)
				return false
		}
		return true
	}
}