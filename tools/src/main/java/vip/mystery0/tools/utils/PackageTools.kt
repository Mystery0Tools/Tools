package vip.mystery0.tools.utils

import vip.mystery0.tools.ToolsClient

object PackageTools {
	/**
	 * 判断手机是否安装某个应用
	 *
	 * @param arrayList 应用包名
	 * @return true：安装，false：未安装
	 */
	fun isApplicationAvailable(arrayList: ArrayList<String>): Boolean {
		val packageManager = ToolsClient.getContext().packageManager// 获取packageManager
		val installedPackages = packageManager.getInstalledPackages(0)
				.map { it.packageName }// 获取所有已安装程序的包信息
		return installedPackages.containsAll(arrayList)
	}
}