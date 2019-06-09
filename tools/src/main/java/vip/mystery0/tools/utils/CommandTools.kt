package vip.mystery0.tools.utils

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

class CommandTools private constructor() {
	companion object {
		private val TAG = CommandTools::class.java.simpleName
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
		private const val PERMISSION_DENIED = "Permission denied"
		private const val CMD_SU = "su"
		private const val CMD_EXIT = "exit\n"
		private const val CMD_LINE_END = "\n"
		private const val CMD_START = "sh"
	}

	private object Holder {
		val holder = CommandTools()
	}
	var isDebug = false

	/**
	 * 申请Root权限
	 * @return 返回申请结果
	 */
	fun requestSU(): Boolean {
		val commandResult = execCommand(emptyArray(), true)
		if (isDebug) {
			Log.i(TAG, commandResult.toString())
		}
		if (commandResult.errorMessage != null && (commandResult.errorMessage == PERMISSION_DENIED))
			return false
		return true
	}

	/**
	 * 执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execCommand(cmd: String): CommandResult = execCommand(arrayOf(cmd), false)

	/**
	 * 申请Root权限之后执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execRootCommand(cmd: String): CommandResult = execCommand(arrayOf(cmd), true)

	/**
	 * 执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execCommands(cmds: Array<String>): CommandResult = execCommand(cmds, false)

	/**
	 * Root后执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execRootCommands(cmds: Array<String>): CommandResult = execCommand(cmds, true)

	private fun execCommand(commands: Array<String>, isRoot: Boolean): CommandResult {
		var result = -1
		var process: Process? = null
		var successResult: BufferedReader? = null
		var errorResult: BufferedReader? = null
		var successMsg: StringBuilder? = null
		var errorMsg: StringBuilder? = null
		var outputStream: DataOutputStream? = null
		try {
			if (isDebug) {
				Log.i(TAG, "exec: ${if (isRoot) CMD_SU else CMD_START}")
			}
			process = Runtime.getRuntime().exec(if (isRoot) CMD_SU else CMD_START)
			outputStream = DataOutputStream(process.outputStream)
			commands.forEach {
				if (isDebug) {
					Log.i(TAG, "exec: $it")
				}
				outputStream.write(it.toByteArray())
				outputStream.writeBytes(CMD_LINE_END)
				outputStream.flush()
			}
			if (isDebug) {
				Log.i(TAG, "exec: $CMD_EXIT")
			}
			outputStream.writeBytes(CMD_EXIT)
			outputStream.flush()

			result = process.waitFor()
			successMsg = StringBuilder()
			errorMsg = StringBuilder()
			successResult = BufferedReader(InputStreamReader(process.inputStream))
			errorResult = BufferedReader(InputStreamReader(process.errorStream))
			var s = successResult.readLine()
			while (s != null) {
				successMsg.append(s)
				s = successResult.readLine()
			}
			s = errorResult.readLine()
			while (s != null) {
				errorMsg.append(s)
				s = errorResult.readLine()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			outputStream?.close()
			successResult?.close()
			errorResult?.close()
			process?.destroy()
		}
		return CommandResult(result, successMsg?.toString(), errorMsg?.toString())
	}

	/**
	 * 执行shell命令的结果
	 */
	data class CommandResult(var result: Int,
							 var successMessage: String? = null,
							 var errorMessage: String? = null) {
		fun isNoRoot(): Boolean = errorMessage != null && errorMessage!!.startsWith(PERMISSION_DENIED)
	}
}