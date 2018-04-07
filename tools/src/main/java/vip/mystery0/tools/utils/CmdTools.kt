package vip.mystery0.tools.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

object CmdTools {
	private const val CMD_SU = "su"
	private const val CMD_EXIT = "exit\n"
	private const val CMD_LINE_END = '\n'

	class CommandResult {
		var result = -1
		var successMessage: String? = null
		var errorMessage: String? = null
	}

	@JvmStatic
	fun isRoot(): Boolean {
		val commandResult = execCmd(CMD_SU)
		return commandResult.result != -1
	}

	@JvmStatic
	fun execCmd(cmd: String): CommandResult {
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

	@JvmStatic
	fun execRootCmd(cmd: String): CommandResult {
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
}