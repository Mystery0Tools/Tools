package vip.mystery0.tools.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vip.mystery0.tools.dispatchMessage
import vip.mystery0.tools.doByTry
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

fun hasSu(): Boolean {
	val commandTools = CommandTools()
	val commandResult = commandTools.execRootCommands(emptyArray())
	Log.i("hasSu", commandResult.toString())
	commandResult.error != null
	if (commandResult.error != null && (commandResult.error!!.contains(CommandTools.PERMISSION_DENIED)))
		return false
	return true
}

suspend fun hasSuInBackground(): Boolean = withContext(Dispatchers.Default) {
	hasSu()
}

class CommandTools {
	companion object {
		private const val TAG = "CommandTools"
		const val PERMISSION_DENIED = "Permission denied"
		private const val CMD_SU = "su"
		private const val CMD_EXIT = "exit\n"
		private const val CMD_LINE_END = "\n"
		private const val CMD_START = "sh"
	}

	var isDebug = false

	/**
	 * 执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execCommand(cmd: String): CommandResult = execCommand(arrayOf(cmd), false)

	suspend fun execCommandInBackground(cmd: String): CommandResult = withContext(Dispatchers.Default) {
		execCommand(cmd)
	}

	/**
	 * 申请Root权限之后执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execRootCommand(cmd: String): CommandResult = execCommand(arrayOf(cmd), true)

	suspend fun execRootCommandInBackground(cmd: String): CommandResult = withContext(Dispatchers.Default) {
		execCommand(cmd)
	}

	/**
	 * 执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execCommands(cmds: Array<String>): CommandResult = execCommand(cmds, false)

	suspend fun execCommandsInBackground(cmds: Array<String>): CommandResult = withContext(Dispatchers.Default) {
		execCommands(cmds)
	}

	/**
	 * Root后执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execRootCommands(cmds: Array<String>): CommandResult = execCommand(cmds, true)

	suspend fun execRootCommandsInBackground(cmds: Array<String>): CommandResult = withContext(Dispatchers.Default) {
		execCommands(cmds)
	}

	private fun execCommand(commands: Array<String>, isRoot: Boolean): CommandResult {
		if (isDebug) {
			Log.i(TAG, "exec: ${if (isRoot) CMD_SU else CMD_START}")
		}
		val pair = doByTry {
			val process = Runtime.getRuntime().exec(if (isRoot) CMD_SU else CMD_START)
			DataOutputStream(process.outputStream).use { outputStream ->
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
			}

			val result = process.waitFor()
			val successMessage = BufferedReader(InputStreamReader(process.inputStream)).readText()
			val errorMessage = BufferedReader(InputStreamReader(process.errorStream)).readText()
			CommandResult(result, successMessage, errorMessage)
		}
		return if (pair.first != null) {
			pair.first!!
		} else {
			Log.w(TAG, pair.second)
			CommandResult(-1, null, pair.second.dispatchMessage())
		}
	}

	/**
	 * 执行shell命令的结果
	 */
	data class CommandResult(var result: Int,
							 var message: String? = null,
							 var error: String? = null) {
		fun isNoRoot(): Boolean = error != null && error!!.startsWith(PERMISSION_DENIED)

		override fun toString(): String = "result: [$result], success: [$message], error: [$error]"
	}
}