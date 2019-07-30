package vip.mystery0.tools.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatTime(): String = TimeTools.instance.formatTime(this)
fun Calendar.equalsDate(calendar: Calendar): Boolean = TimeTools.instance.equalsDate(this, calendar)
fun Calendar.equalsTime(calendar: Calendar): Boolean = TimeTools.instance.equalsTime(this, calendar)
fun Long.toCalendar(): Calendar = TimeTools.instance.getCalendarFromLong(this)
fun Calendar.toDateTimeString(): String = TimeTools.instance.toDateTimeString(this)
fun Calendar.toDateString(): String = TimeTools.instance.toDateString(this)
fun Calendar.toTimeString(): String = TimeTools.instance.toTimeString(this)

class TimeTools private constructor() {
	companion object {
		@JvmField
		val INSTANCE = Holder.holder
		@JvmField
		val instance = INSTANCE
	}

	private object Holder {
		val holder = TimeTools()
	}

	/**
	 * 毫秒转化时分秒毫秒
	 * @param ms
	 * @return
	 */
	fun formatTime(ms: Long): String {
		val ss = 1000
		val mi = ss * 60
		val hh = mi * 60
		val dd = hh * 24

		val day = ms / dd
		val hour = (ms - day * dd) / hh
		val minute = (ms - day * dd - hour * hh) / mi
		val second = (ms - day * dd - hour * hh - minute * mi) / ss
		val milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss

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

	fun equalsDate(calendar1: Calendar, calendar2: Calendar): Boolean = showDateFormat.format(calendar1.time) == showDateFormat.format(calendar2.time)
	fun equalsTime(calendar1: Calendar, calendar2: Calendar): Boolean = showTimeFormat.format(calendar1.time) == showTimeFormat.format(calendar2.time)

	fun getCalendarFromLong(long: Long): Calendar {
		val calendar = Calendar.getInstance()
		calendar.timeInMillis = long
		return calendar
	}

	fun toDateTimeString(calendar: Calendar): String = simpleDateFormat.format(calendar.time)
	fun toDateString(calendar: Calendar): String = showDateFormat.format(calendar.time)
	fun toTimeString(calendar: Calendar): String = showTimeFormat.format(calendar.time)
}