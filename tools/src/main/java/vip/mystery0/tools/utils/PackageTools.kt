package vip.mystery0.tools.utils

import android.os.Build
import vip.mystery0.tools.ToolsClient

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

fun sdkIsAfter(code: Int, exclude: Boolean = false): Boolean = if (exclude) Build.VERSION.SDK_INT > code else Build.VERSION.SDK_INT >= code

fun sdkIsBefore(code: Int, exclude: Boolean = false): Boolean = if (exclude) Build.VERSION.SDK_INT < code else Build.VERSION.SDK_INT <= code

private val nameMap = hashMapOf(
		Pair(1, "1.0"),
		Pair(2, "Petit Four"),
		Pair(3, "Cupcake"),
		Pair(4, "Donut"),
		Pair(5, "Eclair"),
		Pair(6, "Eclair"),
		Pair(7, "Eclair"),
		Pair(8, "Froyo"),
		Pair(9, "Gingerbread"),
		Pair(10, "Gingerbread"),
		Pair(11, "Honeycomb"),
		Pair(12, "Honeycomb"),
		Pair(13, "Honeycomb"),
		Pair(14, "Ice Cream Sandwich"),
		Pair(15, "Ice Cream Sandwich"),
		Pair(16, "Jelly Bean"),
		Pair(17, "Jelly Bean"),
		Pair(18, "Jelly Bean"),
		Pair(19, "KitKat"),
		Pair(20, "KitKat"),
		Pair(21, "Lollipop"),
		Pair(22, "Lollipop"),
		Pair(23, "Marshmallow"),
		Pair(24, "Nougat"),
		Pair(25, "Nougat"),
		Pair(26, "Oreo"),
		Pair(27, "Oreo"),
		Pair(28, "Pie"),
		Pair(29, "10"),
		Pair(30, "11")
)

fun Int.getAndroidName(): String {
	if (this <= 0 || this > nameMap.keys.size)
		return this.toString()
	return nameMap.getValue(this)
}

object AndroidVersionCode {
	const val VERSION_BASE = Build.VERSION_CODES.BASE
	const val VERSION_BASE_1_1 = Build.VERSION_CODES.BASE_1_1
	const val VERSION_CUPCAKE = Build.VERSION_CODES.CUPCAKE
	const val VERSION_DONUT = Build.VERSION_CODES.DONUT
	const val VERSION_ECLAIR = Build.VERSION_CODES.ECLAIR
	const val VERSION_ECLAIR_0_1 = Build.VERSION_CODES.ECLAIR_0_1
	const val VERSION_ECLAIR_MR1 = Build.VERSION_CODES.ECLAIR_MR1
	const val VERSION_FROYO = Build.VERSION_CODES.FROYO
	const val VERSION_GINGERBREAD = Build.VERSION_CODES.GINGERBREAD
	const val VERSION_GINGERBREAD_MR1 = Build.VERSION_CODES.GINGERBREAD_MR1
	const val VERSION_HONEYCOMB = Build.VERSION_CODES.HONEYCOMB
	const val VERSION_HONEYCOMB_MR1 = Build.VERSION_CODES.HONEYCOMB_MR1
	const val VERSION_HONEYCOMB_MR2 = Build.VERSION_CODES.HONEYCOMB_MR2
	const val VERSION_ICE_CREAM_SANDWICH = Build.VERSION_CODES.ICE_CREAM_SANDWICH
	const val VERSION_ICE_CREAM_SANDWICH_MR1 = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
	const val VERSION_JELLY_BEAN = Build.VERSION_CODES.JELLY_BEAN
	const val VERSION_JELLY_BEAN_MR1 = Build.VERSION_CODES.JELLY_BEAN_MR1
	const val VERSION_JELLY_BEAN_MR2 = Build.VERSION_CODES.JELLY_BEAN_MR2
	const val VERSION_KITKAT = Build.VERSION_CODES.KITKAT
	const val VERSION_KITKAT_WATCH = Build.VERSION_CODES.KITKAT_WATCH
	const val VERSION_LOLLIPOP = Build.VERSION_CODES.LOLLIPOP
	const val VERSION_LOLLIPOP_MR1 = Build.VERSION_CODES.LOLLIPOP_MR1
	const val VERSION_M = Build.VERSION_CODES.M
	const val VERSION_N = Build.VERSION_CODES.N
	const val VERSION_N_MR1 = Build.VERSION_CODES.N_MR1
	const val VERSION_O = Build.VERSION_CODES.O
	const val VERSION_O_MR1 = Build.VERSION_CODES.O_MR1
	const val VERSION_P = Build.VERSION_CODES.P
	const val VERSION_Q = Build.VERSION_CODES.Q
	const val VERSION_R = Build.VERSION_CODES.R
}