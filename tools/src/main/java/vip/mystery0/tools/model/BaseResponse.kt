package vip.mystery0.tools.model

open class BaseResponse {
	var code: Int = 0
	var message: String? = null

	val isSuccessful: Boolean get() = code == 0
}