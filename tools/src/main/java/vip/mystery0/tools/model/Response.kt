package vip.mystery0.tools.model

class Response<T> {
	var code: Int = 0
	var data: T? = null
	var message: String? = null

	val isSuccessful: Boolean get() = code == 0

	fun verify(): T? = if (isSuccessful) data else throw Exception(message)
}