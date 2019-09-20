package vip.mystery0.tools.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import vip.mystery0.tools.context
import java.io.File

/**
 * 分享单个文件
 * @param titleString   分享选择界面显示的文本
 * @param type          分享的类型
 * @param authorities   创建Uri时需要的
 */
fun File.shareFile(authorities: String? = null,
				   type: String = "*/*",
				   titleString: String = "") {
	val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		FileProvider.getUriForFile(context(), authorities!!, this)
	else
		Uri.fromFile(this)
	uri.shareFile(type, titleString)
}

fun ArrayList<File>.shareMultFile(authorities: String? = null,
								  type: String = "*/*",
								  titleString: String = "") {
	val urlList = ArrayList<Uri>()
	mapTo(urlList) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			FileProvider.getUriForFile(context(), authorities!!, it)
		else
			Uri.fromFile(it)
	}
	urlList.shareMultUri(type, titleString)
}

/**
 * 分享多个文件
 * @param titleString    分享选择界面显示的文本
 * @param type           分享的类型
 */
fun ArrayList<Uri>.shareMultUri(type: String = "*/*",
								titleString: String = "") {
	val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
	intent.type = type
	intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, this)
	context().startActivity(Intent.createChooser(intent, titleString))
}

/**
 * 分享单个Uri
 * @param titleString   分享选择界面显示的文本
 * @param type          分享的类型
 */
fun Uri.shareFile(type: String = "*/*",
				  titleString: String = "") {
	val intent = Intent(Intent.ACTION_SEND)
	intent.type = type
	intent.putExtra(Intent.EXTRA_STREAM, this)
	grantUriPermission(intent)
	context().startActivity(Intent.createChooser(intent, titleString))
}

/**
 * 给分享的Uri授权
 * @param intent    分享的意图
 */
fun Uri.grantUriPermission(intent: Intent) {
	context().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
			.forEach {
				context().grantUriPermission(it.activityInfo.packageName, this, Intent.FLAG_GRANT_READ_URI_PERMISSION)
			}
}