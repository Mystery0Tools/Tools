package vip.mystery0.tools.utils

import android.os.Build
import androidx.annotation.RequiresApi
import vip.mystery0.tools.model.LRUCache
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

private val formatterLRUCache by lazy { LRUCache<DateTimeFormatter>(5) }

@RequiresApi(Build.VERSION_CODES.O)
fun getFormatter(pattern: String): DateTimeFormatter = formatterLRUCache.getOrElse(pattern) {
	val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
	formatterLRUCache[pattern] = formatter
	formatter
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatDate(): String = formatLocalDataTime(getFormatter("yyyy-MM-dd"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.formatDate(): String = atStartOfDay().formatLocalDataTime(getFormatter("yyyy-MM-dd"))

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatDate(): String = formatInstant(getFormatter("yyyy-MM-dd"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatTime(): String = formatLocalDataTime(getFormatter("HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.formatTime(): String = atDate(LocalDate.now()).formatLocalDataTime(getFormatter("HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatTime(): String = formatInstant(getFormatter("HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatDateTime(): String = formatLocalDataTime(getFormatter("yyyy-MM-dd HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatDateTime(): String = formatInstant(getFormatter("yyyy-MM-dd HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun String.parseDate(): LocalDate = LocalDate.parse(this, getFormatter("yyyy-MM-dd"))

@RequiresApi(Build.VERSION_CODES.O)
fun String.parseTime(): LocalTime = LocalTime.parse(this, getFormatter("HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun String.parseDateTime(): LocalDateTime = LocalDateTime.parse(this, getFormatter("yyyy-MM-dd HH:mm:ss"))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatLocalDataTime(pattern: String? = null): String =
		formatLocalDataTime(if (pattern == null) getFormatter("yyyy-MM-dd HH:mm:ss") else getFormatter(pattern))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatLocalDataTime(dateTimeFormatter: DateTimeFormatter = getFormatter("yyyy-MM-dd HH:mm:ss")): String =
		dateTimeFormatter.format(this)

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatInstant(pattern: String? = null): String =
		formatInstant(if (pattern == null) getFormatter("yyyy-MM-dd HH:mm:ss") else getFormatter(pattern))

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatInstant(dateTimeFormatter: DateTimeFormatter = getFormatter("yyyy-MM-dd HH:mm:ss")): String =
		LocalDateTime.ofInstant(this, ZoneId.systemDefault()).formatLocalDataTime(dateTimeFormatter)