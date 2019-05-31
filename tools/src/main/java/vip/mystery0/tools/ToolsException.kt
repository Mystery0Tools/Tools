package vip.mystery0.tools

open class ToolsException(val code: Int, override val message: String?) : RuntimeException() {
	companion object {
		const val ERROR = 101
		const val FILE_NOT_EXIST = 102
		const val MAKE_DIR_ERROR = 103
		const val NOT_FILE = 104
		const val NOT_DIRECTORY = 105
	}

	constructor(e: Throwable) : this(ERROR, e.message)
	constructor(message: String?) : this(ERROR, message)
	constructor() : this(ERROR, null)
}