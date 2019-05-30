package vip.mystery0.tools.utils

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
	fun formatTime(ms: Long?): String {
		val ss = 1000
		val mi = ss * 60
		val hh = mi * 60
		val dd = hh * 24

		val day = ms!! / dd
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
}