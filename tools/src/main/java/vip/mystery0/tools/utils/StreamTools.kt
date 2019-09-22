package vip.mystery0.tools.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vip.mystery0.tools.tryOrBoolean
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "StreamTools"

class PairStream<I : InputStream, O : OutputStream>(private val input: I, private val output: O) {
	fun copy(bufferSize: Int = 8024,
			 closeInput: Boolean = true,
			 closeOutput: Boolean = true): Long {
		val buffer = ByteArray(bufferSize)
		var len = input.read(buffer)
		var count: Long = 0
		while (len > 0) {
			output.write(buffer, 0, len)
			count += len.toLong()
			len = input.read(buffer)
		}
		if (closeInput)
			input.closeQuietly(true)
		if (closeOutput)
			output.closeQuietly(true)
		return count
	}
}

/**
 * 拷贝流
 * @param output 输出流
 * @param bufferSize 缓冲区大小
 */
suspend fun <T : InputStream> T?.copy(output: OutputStream?,
									  bufferSize: Int = 8024): Long {
	require(bufferSize >= 1) { "缓冲区大小必须大于0" }
	requireNotNull(this) { "输入流不能为空" }
	requireNotNull(output) { "输出流不能为空" }
	return withContext(Dispatchers.IO) {
		PairStream(this@copy, output).copy(bufferSize)
	}
}

fun <T : Closeable?> T.useToBoolean(block: (T) -> Unit): Boolean = this.tryOrBoolean {
	use {
		block(it)
		return@use true
	}
}

fun <T : Closeable?> T.closeQuietly(log: Boolean = false) {
	if (this != null) {
		try {
			close()
		} catch (e: IOException) {
			if (log) {
				Log.e(TAG, "closeQuietly: ", e)
			}
		}
	}
}