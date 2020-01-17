package vip.mystery0.tools.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 毫秒转化时分秒毫秒
 * @return
 */
fun Long.formatTime(): String {
	if (this <= 0) return "0毫秒"
	val ss = 1000
	val mi = ss * 60
	val hh = mi * 60
	val dd = hh * 24

	val day = this / dd
	val hour = (this - day * dd) / hh
	val minute = (this - day * dd - hour * hh) / mi
	val second = (this - day * dd - hour * hh - minute * mi) / ss
	val milliSecond = this % ss

	val sb = StringBuffer()
	if (day > 0) {
		sb.append(day.toString() + "天")
	}
	if (hour > 0) {
		sb.append(hour.toString() + "小时")
	}
	if (minute > 0) {
		sb.append(minute.toString() + "分")
	}
	if (second > 0) {
		sb.append(second.toString() + "秒")
	}
	if (milliSecond > 0) {
		sb.append(milliSecond.toString() + "毫秒")
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