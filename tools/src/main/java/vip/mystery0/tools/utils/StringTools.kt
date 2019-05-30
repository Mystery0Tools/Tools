package vip.mystery0.tools.utils

import java.security.MessageDigest

class StringTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = StringTools()
	}

	/**
	 * MD5 加密
	 * @param message 原始数据
	 * @return MD5 加密之后的字符串
	 */
	fun md5(message: String): String {
		val digest = MessageDigest.getInstance("MD5")
		val result = digest.digest(message.toByteArray())
		return toHex(result)
	}

	/**
	 * SHA-1 加密
	 * @param message 原始数据
	 * @return SHA-1 加密之后的字符串
	 */
	fun sha1(message: String): String {
		val digest = MessageDigest.getInstance("SHA-1")
		val result = digest.digest(message.toByteArray())
		return toHex(result)
	}

	/**
	 * SHA-256 加密
	 * @param message 原始数据
	 * @return SHA-256 加密之后的字符串
	 */
	fun sha256(message: String): String {
		val digest = MessageDigest.getInstance("SHA-256")
		val result = digest.digest(message.toByteArray())
		return toHex(result)
	}

	/**
	 * 将指定byte数组转换为16进制字符串
	 * @param byteArray 原始数据
	 * @return 转换之后数据
	 */
	private fun toHex(byteArray: ByteArray): String =
			with(StringBuilder()) {
				byteArray.forEach {
					val value = it
					val hex = value.toInt() and (0xFF)
					val hexStr = Integer.toHexString(hex)
					if (hexStr.length == 1)
						append("0").append(hexStr)
					else
						append(hexStr)
				}
				toString()
			}
}