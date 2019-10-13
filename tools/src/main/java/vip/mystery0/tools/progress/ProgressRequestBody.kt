package vip.mystery0.tools.progress

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

class ProgressRequestBody(private val requestBody: RequestBody,
						  private val progress: (Long, Long) -> Unit) : RequestBody() {
	private var bufferedSink: BufferedSink? = null

	override fun contentLength(): Long = requestBody.contentLength()

	override fun contentType(): MediaType? = requestBody.contentType()

	override fun writeTo(sink: BufferedSink) {
		if (bufferedSink == null)
			bufferedSink = CountSink(sink).buffer()
		requestBody.writeTo(bufferedSink!!)
		bufferedSink?.flush()
	}

	private inner class CountSink(delegate: Sink) : ForwardingSink(delegate) {
		private var totalByteWrite = 0L

		override fun write(source: Buffer, byteCount: Long) {
			super.write(source, byteCount)

			totalByteWrite += byteCount
			progress(totalByteWrite, contentLength())
		}
	}
}