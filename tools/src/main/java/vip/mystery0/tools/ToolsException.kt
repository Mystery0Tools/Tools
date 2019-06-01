package vip.mystery0.tools

open class ToolsException(val code: Int, override val message: String?) : RuntimeException() {
	companion object {
		const val ERROR = 101
		const val FILE_NOT_EXIST = 102
		const val FILE_EXIST = 103
		const val MAKE_DIR_ERROR = 104
		const val NOT_FILE = 105
		const val NOT_DIRECTORY = 106
	}

	constructor(e: Throwable) : this(ERROR, e.message)
	constructor(message: String?) : this(ERROR, message)
	constructor() : this(ERROR, null)
}