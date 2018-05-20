package vip.mystery0.tools.utils

import java.math.BigInteger
import java.security.MessageDigest

object StringTools {
	fun getMD5(message: String): String {
		try {
			val md5 = MessageDigest.getInstance("MD5")
			md5.update(message.toByteArray())
			val bigInteger = BigInteger(1, md5.digest())
			return bigInteger.toString(16)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return "ERROR"
	}
}