package vip.mystery0.tools.model

data class Response<T>(
		var code: Int,
		var data: T?,
		var message: String?
) {
	val isSuccessful: Boolean get() = code == 0

	fun verify(): T? = if (isSuccessful) data else throw Exception(message)
}