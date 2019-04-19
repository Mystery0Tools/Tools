package vip.mystery0.tools.factory

import java.io.File

object MimeTypeFactory {
	@JvmStatic
	fun typeOfFilePath(filePath: String): String = typeOfFile(File(filePath))

	@JvmStatic
	fun typeOfFile(file: File): String = typeOfExtension(file.extension)

	@JvmStatic
	fun typeOfFileName(fileName: String): String {
		val ext = fileName.substringAfterLast('.')
		return typeOfExtension(ext)
	}

	@JvmStatic
	fun typeOfExtension(ext: String): String {
		val low = ext.toLowerCase()
		if (map.containsKey(low))
			return map.getValue(low)
		return map.getValue("")
	}

	@JvmStatic
	private val map = hashMapOf(
			Pair("3gp", "video/3gpp"),
			Pair("apk", "application/vnd.android.package-archive"),
			Pair("asf", "video/x-ms-asf"),
			Pair("avi", "video/x-msvideo"),
			Pair("bin", "application/octet-stream"),
			Pair("bmp", "image/bmp"),
			Pair("c", "text/plain"),
			Pair("class", "application/octet-stream"),
			Pair("conf", "text/plain"),
			Pair("cpp", "text/plain"),
			Pair("doc", "application/msword"),
			Pair("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
			Pair("xls", "application/vnd.ms-excel"),
			Pair("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
			Pair("exe", "application/octet-stream"),
			Pair("gif", "image/gif"),
			Pair("gtar", "application/x-gtar"),
			Pair("gz", "application/x-gzip"),
			Pair("h", "text/plain"),
			Pair("htm", "text/html"),
			Pair("html", "text/html"),
			Pair("jar", "application/java-archive"),
			Pair("java", "text/plain"),
			Pair("jpeg", "image/jpeg"),
			Pair("jpg", "image/jpeg"),
			Pair("js", "application/x-javascript"),
			Pair("log", "text/plain"),
			Pair("m3u", "audio/x-mpegurl"),
			Pair("m4a", "audio/mp4a-latm"),
			Pair("m4b", "audio/mp4a-latm"),
			Pair("m4p", "audio/mp4a-latm"),
			Pair("m4u", "video/vnd.mpegurl"),
			Pair("m4v", "video/x-m4v"),
			Pair("mov", "video/quicktime"),
			Pair("mp2", "audio/x-mpeg"),
			Pair("mp3", "audio/x-mpeg"),
			Pair("mp4", "video/mp4"),
			Pair("mpc", "application/vnd.mpohun.certificate"),
			Pair("mpe", "video/mpeg"),
			Pair("mpeg", "video/mpeg"),
			Pair("mpg", "video/mpeg"),
			Pair("mpg4", "video/mp4"),
			Pair("mpga", "audio/mpeg"),
			Pair("msg", "application/vnd.ms-outlook"),
			Pair("ogg", "audio/ogg"),
			Pair("pdf", "application/pdf"),
			Pair("png", "image/png"),
			Pair("pps", "application/vnd.ms-powerpoint"),
			Pair("ppt", "application/vnd.ms-powerpoint"),
			Pair("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
			Pair("prop", "text/plain"),
			Pair("rc", "text/plain"),
			Pair("rmvb", "audio/x-pn-realaudio"),
			Pair("rtf", "application/rtf"),
			Pair("sh", "text/plain"),
			Pair("tar", "application/x-tar"),
			Pair("tgz", "application/x-compressed"),
			Pair("txt", "text/plain"),
			Pair("wav", "audio/x-wav"),
			Pair("wma", "audio/x-ms-wma"),
			Pair("wmv", "audio/x-ms-wmv"),
			Pair("wps", "application/vnd.ms-works"),
			Pair("xml", "text/plain"),
			Pair("z", "application/x-compress"),
			Pair("zip", "application/x-zip-compressed"),
			Pair("", "*/*"))
}