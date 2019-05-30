package vip.mystery0.tools.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class IntentTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = IntentTools()
	}

	/**
	 * 分享单个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param file          分享的文件
	 * @param authorities   创建Uri时需要的
	 */
	fun shareFile(context: Context, titleString: String, file: File, authorities: String?) {
		shareFile(context, titleString, "*/*", file, authorities)
	}

	/**
	 * 分享单个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param type          分享的类型
	 * @param file          分享的文件
	 * @param authorities   创建Uri时需要的
	 */
	fun shareFile(context: Context, titleString: String, type: String, file: File, authorities: String?) {
		val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			FileProvider.getUriForFile(context, authorities!!, file)
		else
			Uri.fromFile(file)
		shareFile(context, titleString, type, uri)
	}

	/**
	 * 分享单个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param uri           分享的文件的Uri
	 */
	fun shareFile(context: Context, titleString: String, uri: Uri) {
		shareFile(context, titleString, "*/*", uri)
	}

	/**
	 * 分享单个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param type          分享的类型
	 * @param uri           分享的文件的Uri
	 */
	fun shareFile(context: Context, titleString: String, type: String, uri: Uri) {
		val intent = Intent(Intent.ACTION_SEND)
		intent.type = type
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		grantUriPermission(context, intent, uri)
		context.startActivity(Intent.createChooser(intent, titleString))
	}

	/**
	 * 分享多个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param fileList      分享的文件列表
	 * @param authorities   创建Uri时需要的
	 */
	fun shareMultFile(context: Context, titleString: String, fileList: ArrayList<File>, authorities: String?) {
		shareMultFile(context, titleString, "*/*", fileList, authorities)
	}

	/**
	 * 分享多个文件
	 * @param context       上下文
	 * @param titleString   分享选择界面显示的文本
	 * @param type          分享的类型
	 * @param fileList      分享的文件列表
	 * @param authorities   创建Uri时需要的
	 */
	fun shareMultFile(context: Context, titleString: String, type: String, fileList: ArrayList<File>, authorities: String?) {
		val uriList = ArrayList<Uri>()
		fileList.forEach {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				uriList.add(FileProvider.getUriForFile(context, authorities!!, it))
			else
				uriList.add(Uri.fromFile(it))
		}
		shareMultFile(context, titleString, type, uriList)
	}

	/**
	 * 分享多个文件
	 * @param context        上下文
	 * @param titleString    分享选择界面显示的文本
	 * @param uriList        分享的Uri列表
	 */
	fun shareMultFile(context: Context, titleString: String, uriList: ArrayList<Uri>) {
		shareMultFile(context, titleString, "*/*", uriList)
	}

	/**
	 * 分享多个文件
	 * @param context        上下文
	 * @param titleString    分享选择界面显示的文本
	 * @param type           分享的类型
	 * @param uriList        分享的Uri列表
	 */
	fun shareMultFile(context: Context, titleString: String, type: String, uriList: ArrayList<Uri>) {
		val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
		intent.type = type
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
		context.startActivity(Intent.createChooser(intent, titleString))
	}

	/**
	 * 给分享的Uri授权
	 * @param context   上下文
	 * @param intent    分享的意图
	 * @param uri       要授权的Uri
	 */
	fun grantUriPermission(context: Context, intent: Intent, uri: Uri) {
		context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
				.forEach {
					context.grantUriPermission(it.activityInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
				}
	}
}