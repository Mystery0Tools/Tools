package vip.mystery0.tools.utils

import java.security.MessageDigest

/**
 * MD5 加密
 * @return MD5 加密之后的字符串
 */
fun String.md5(): String {
	val digest = MessageDigest.getInstance("MD5")
	val result = digest.digest(toByteArray())
	return toHex(result)
}

/**
 * SHA-1 加密
 * @return SHA-1 加密之后的字符串
 */
fun String.sha1(): String {
	val digest = MessageDigest.getInstance("SHA-1")
	val result = digest.digest(toByteArray())
	return toHex(result)
}

/**
 * SHA-256 加密
 * @return SHA-256 加密之后的字符串
 */
fun String.sha256(): String {
	val digest = MessageDigest.getInstance("SHA-256")
	val result = digest.digest(toByteArray())
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