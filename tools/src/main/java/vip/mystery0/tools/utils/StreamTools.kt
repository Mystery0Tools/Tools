package vip.mystery0.tools.utils

import android.util.Log
import vip.mystery0.tools.tryOrBoolean
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "StreamTools"

/**
 * 拷贝流
 * @param output 输出流
 * @param bufferSize 缓冲区大小
 */
fun <T : InputStream> T?.copy(output: OutputStream?,
							  bufferSize: Int = 8024): Long {
	require(bufferSize >= 1) { "缓冲区大小必须大于0" }
	requireNotNull(this) { "输入流不能为空" }
	requireNotNull(output) { "输出流不能为空" }
	val buffer = ByteArray(bufferSize)
	var n = read(buffer)
	var count: Long = 0
	while (n > 0) {
		output.write(buffer, 0, n)
		count += n.toLong()
		n = read(buffer)
	}
	return count
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