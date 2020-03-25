package vip.mystery0.tools.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 毫秒转化时分秒毫秒
 * @return
 */

enum class TimeUnit(val level: Int, val unit: String, val interval: Int) {
	MILLISECOND(0, "毫秒", 1000),
	SECOND(1, "秒", 60),
	MINUTE(2, "分", 60),
	HOUR(3, "小时", 24),
	DAY(4, "天", 1)
}

private fun getTimeUnitByLevel(level: Int): TimeUnit? = when (level) {
	0 -> TimeUnit.MILLISECOND
	1 -> TimeUnit.SECOND
	2 -> TimeUnit.MINUTE
	3 -> TimeUnit.HOUR
	4 -> TimeUnit.DAY
	else -> null
}

fun Long.formatTime(
		minTimeUnit: TimeUnit = TimeUnit.MILLISECOND,
		maxTimeUnit: TimeUnit = TimeUnit.DAY
): String {
	if (minTimeUnit.level > maxTimeUnit.level) {
		//等级不正确，抛出异常
		throw NumberFormatException("等级设置错误")
	}
	val ss = 1000
	val mi = ss * 60
	val hh = mi * 60
	val dd = hh * 24

	if (this <= 0) return "0${minTimeUnit.unit}"
	if (maxTimeUnit == TimeUnit.MILLISECOND) return "$this${TimeUnit.MILLISECOND.unit}"

	val day = this / dd
	val hour = (this - day * dd) / hh
	val minute = (this - day * dd - hour * hh) / mi
	val second = (this - day * dd - hour * hh - minute * mi) / ss
	val milliSecond = this % ss
	val array = arrayOf(day, hour, minute, second, milliSecond)
	val sb = StringBuffer()
	for (index in array.indices) {
		val unit = getTimeUnitByLevel(array.size - index - 1)!!
		val nextUnit = getTimeUnitByLevel(array.size - index - 2)
		if (array[index] > 0) {
			if (maxTimeUnit.level < unit.level) {
				if (nextUnit != null)
					array[index + 1] += array[index] * nextUnit.interval
			} else {
				sb.append(array[index]).append(unit.unit)
			}
		}
		if (minTimeUnit == unit) {
			if (sb.isEmpty()) sb.append(0).append(minTimeUnit.unit)
			return sb.toString()
		}
	}
	return sb.toString()
}

private val simpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA) }
private val showDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd", Locale.CHINA) }
private val showTimeFormat by lazy { SimpleDateFormat("HH:mm:ss", Locale.CHINA) }

fun Calendar.equalsDate(calendar: Calendar): Boolean = showDateFormat.format(time) == showDateFormat.format(calendar.time)
fun Calendar.equalsTime(calendar: Calendar): Boolean = showTimeFormat.format(time) == showTimeFormat.format(calendar.time)

fun Long.getCalendarFromLong(): Calendar {
	val calendar = Calendar.getInstance()
	calendar.timeInMillis = this
	return calendar
}

fun Date.toDateTimeString(): String = simpleDateFormat.format(time)
fun Date.toDateString(): String = showDateFormat.format(time)
fun Date.toTimeString(): String = showTimeFormat.format(time)
fun Calendar.toDateTimeString(): String = simpleDateFormat.format(time)
fun Calendar.toDateString(): String = showDateFormat.format(time)
fun Calendar.toTimeString(): String = showTimeFormat.format(time)

fun now(): Calendar = Calendar.getInstance()
fun nowMillis(): Long = now().timeInMillis
fun nowDateTime(): String = now().toDateTimeString()