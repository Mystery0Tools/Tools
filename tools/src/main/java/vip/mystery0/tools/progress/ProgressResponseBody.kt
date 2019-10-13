package vip.mystery0.tools.progress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class DownloadProgressResponseBody(private val responseBody: ResponseBody,
								   private val progress: (Long, Long) -> Unit) : ResponseBody() {
	private var bufferedSource: BufferedSource? = null

	override fun contentType(): MediaType? = responseBody.contentType()

	override fun contentLength(): Long = responseBody.contentLength()

	override fun source(): BufferedSource {
		if (bufferedSource == null)
			bufferedSource = CountSource(responseBody.source()).buffer()
		return bufferedSource!!
	}

	private inner class CountSource(source: Source) : ForwardingSource(source) {
		private var totalBytesRead = 0L

		override fun read(sink: Buffer, byteCount: Long): Long {
			val bytesRead = super.read(sink, byteCount)
			totalBytesRead += if (bytesRead != -1L) bytesRead else 0
			progress(totalBytesRead, contentLength())
			return bytesRead
		}
	}
}