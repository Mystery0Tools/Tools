package vip.mystery0.tools.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

object CmdTools {
	private const val PERMISSION_DENIED = "Permission denied"
	private const val CMD_SU = "su"
	private const val CMD_EXIT = "exit\n"
	private const val CMD_LINE_END = '\n'
	private const val CMD_START = "sh"

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
		var process: Process? = null
		var dataOutputStream: DataOutputStream? = null
		try {
			process = Runtime.getRuntime().exec(cmd)
			dataOutputStream = DataOutputStream(process.outputStream)
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process.inputStream)).readText()
		} catch (e: Exception) {
			e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			if (dataOutputStream != null)
				dataOutputStream.close()
			if (process != null)
				process.destroy()
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
		var process: Process? = null
		var dataOutputStream: DataOutputStream? = null
		try {
			process = Runtime.getRuntime().exec(CMD_SU)
			dataOutputStream = DataOutputStream(process.outputStream)
			dataOutputStream.writeBytes(cmd + CMD_LINE_END)
			dataOutputStream.flush()
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process.inputStream)).readText()
		} catch (e: Exception) {
			e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			if (dataOutputStream != null)
				dataOutputStream.close()
			if (process != null)
				process.destroy()
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
		var process: Process? = null
		var dataOutputStream: DataOutputStream? = null
		try {
			process = Runtime.getRuntime().exec(CMD_START)
			dataOutputStream = DataOutputStream(process.outputStream)
			for (i in cmds.indices) {
				dataOutputStream.writeBytes("${cmds[i]}$CMD_LINE_END")
				dataOutputStream.flush()
			}
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process.inputStream)).readText()
		} catch (e: Exception) {
			e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			if (dataOutputStream != null)
				dataOutputStream.close()
			if (process != null)
				process.destroy()
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

		var process: Process? = null
		var dataOutputStream: DataOutputStream? = null
		try {
			process = Runtime.getRuntime().exec(CMD_SU)
			dataOutputStream = DataOutputStream(process.outputStream)
			for (i in cmds.indices) {
				dataOutputStream.writeBytes("${cmds[i]}$CMD_LINE_END")
				dataOutputStream.flush()
			}
			dataOutputStream.writeBytes(CMD_EXIT)
			dataOutputStream.flush()
			result.result = process.waitFor()
			result.errorMessage = BufferedReader(InputStreamReader(process.errorStream)).readText()
			result.successMessage = BufferedReader(InputStreamReader(process.inputStream)).readText()
		} catch (e: Exception) {
			e.printStackTrace()
			result.errorMessage = e.message
			result.successMessage = ""
		} finally {
			if (dataOutputStream != null)
				dataOutputStream.close()
			if (process != null)
				process.destroy()
		}
		return result
	}

	class CommandResult {
		var result = -1
		var successMessage: String? = null
		var errorMessage: String? = null

		override fun toString(): String {
			return "result=$result, successMessage=$successMessage, errorMessage=$errorMessage"
		}
	}
}