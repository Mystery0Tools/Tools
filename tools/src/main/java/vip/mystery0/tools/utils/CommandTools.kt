package vip.mystery0.tools.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

fun hasSu(): Boolean {
	val commandTools = CommandTools()
	val commandResult = commandTools.execCommands(emptyArray())
	Log.i("hasSu", commandResult.toString())
	if (commandResult.errorMessage != null && (commandResult.errorMessage == CommandTools.PERMISSION_DENIED))
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

	@Throws(IOException::class)
	private fun execCommand(commands: Array<String>, isRoot: Boolean): CommandResult {
		if (isDebug) {
			Log.i(TAG, "exec: ${if (isRoot) CMD_SU else CMD_START}")
		}
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
		return CommandResult(result, successMessage, errorMessage)
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