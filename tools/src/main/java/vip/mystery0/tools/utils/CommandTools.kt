package vip.mystery0.tools.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

object CommandTools {
	private const val PERMISSION_DENIED = "Permission denied"
	private const val CMD_SU = "su"
	private const val CMD_EXIT = "exit\n"
	private const val CMD_LINE_END = '\n'
	private const val CMD_START = "sh"
	private var process: Process? = null
	var isPrintError = true

	/**
	 * 检测设备是否Root
	 * @return 返回是否Root
	 */
	fun isRoot(): Boolean {
		val commandResult = execCommand(CMD_SU)
		return commandResult.result != -1
	}

	/**
	 * 申请Root权限
	 * @return 返回申请结果
	 */
	fun requestSU(): Boolean {
		val commandResult = execCommand(CMD_SU)
		if (commandResult.errorMessage != null && commandResult.errorMessage!!.contains(PERMISSION_DENIED))
			return false
		return true
	}

	/**
	 * 执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execCommand(cmd: String): CommandResult {
		val result = CommandResult()
		var dataOutputStream: DataOutputStream? = null
		try {
			killProcess()
			process = Runtime.getRuntime().exec(cmd)
			dataOutputStream = DataOutputStream(process!!.outputStream)
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process!!.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process!!.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process!!.inputStream)).readText()
		} catch (e: Exception) {
			if (isPrintError)
				e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			dataOutputStream?.close()
			process?.destroy()
			process = null
		}
		return result
	}

	/**
	 * 申请Root权限之后执行命令
	 * @param cmd    执行的命令
	 * @return       返回包含执行结果的对象
	 */
	fun execRootCommand(cmd: String): CommandResult {
		val result = CommandResult()
		var dataOutputStream: DataOutputStream? = null
		try {
			killProcess()
			process = Runtime.getRuntime().exec(CMD_SU)
			dataOutputStream = DataOutputStream(process!!.outputStream)
			dataOutputStream.writeBytes(cmd + CMD_LINE_END)
			dataOutputStream.flush()
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process!!.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process!!.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process!!.inputStream)).readText()
		} catch (e: Exception) {
			if (isPrintError)
				e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			dataOutputStream?.close()
			process?.destroy()
			process = null
		}
		return result
	}

	/**
	 * 执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execCommands(cmds: Array<String>): CommandResult {
		val result = CommandResult()
		var dataOutputStream: DataOutputStream? = null
		try {
			killProcess()
			process = Runtime.getRuntime().exec(CMD_START)
			dataOutputStream = DataOutputStream(process!!.outputStream)
			for (i in cmds.indices) {
				dataOutputStream.writeBytes("${cmds[i]}$CMD_LINE_END")
				dataOutputStream.flush()
			}
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process!!.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process!!.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process!!.inputStream)).readText()
		} catch (e: Exception) {
			if (isPrintError)
				e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			dataOutputStream?.close()
			process?.destroy()
			process = null
		}
		return result
	}

	/**
	 * Root后执行多条命令
	 * @param cmds    执行的命令
	 * @return        返回包含执行结果的对象
	 */
	fun execRootCommands(cmds: Array<String>): CommandResult {
		val result = CommandResult()
		var dataOutputStream: DataOutputStream? = null
		try {
			killProcess()
			process = Runtime.getRuntime().exec(CMD_SU)
			dataOutputStream = DataOutputStream(process!!.outputStream)
			for (i in cmds.indices) {
				dataOutputStream.writeBytes("${cmds[i]}$CMD_LINE_END")
				dataOutputStream.flush()
			}
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process!!.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process!!.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process!!.inputStream)).readText()
		} catch (e: Exception) {
			if (isPrintError)
				e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			dataOutputStream?.close()
			process?.destroy()
			process = null
		}
		return result
	}

	/**
	 * 终止进程
	 */
	fun killProcess() {
		process?.destroy()
	}

	class CommandResult {
		var result = -1
		var successMessage: String? = null
		var errorMessage: String? = null

		fun isSuccess(): Boolean = errorMessage == null || errorMessage == ""

		override fun toString(): String {
			return "result=$result, successMessage=$successMessage, errorMessage=$errorMessage"
		}
	}
}