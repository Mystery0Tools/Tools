package vip.mystery0.tools.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private object SAFFileTools {
	fun mkdirs(context: Context, treeUri: Uri) {
		val file = DocumentFile.fromTreeUri(context, treeUri)
				?: throw NullPointerException("treeUri 无效")
		if (file.exists())
			return
		val parent = file.parentFile ?: throw NullPointerException("parent file 为空")
		if (parent.exists())
			mkdirs(context, parent.uri)
		parent.createDirectory(file.name!!)
	}
}