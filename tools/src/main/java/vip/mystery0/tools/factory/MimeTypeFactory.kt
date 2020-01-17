package vip.mystery0.tools.factory

import org.apache.tika.Tika
import org.apache.tika.mime.MimeTypes
import java.io.File

private val tika = Tika()

val File.mimeType: String
	get() = tika.detect(this)

fun String.getMimeType(): String = tika.detect(this)

fun String.getExtensionFromMimeType(): String? = MimeTypes.getDefaultMimeTypes().forName(this).extension