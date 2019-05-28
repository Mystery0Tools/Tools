package vip.mystery0.tools.factory

import com.google.gson.Gson
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

fun Any.toJson(): String = GsonFactory.gson.toJson(this)

inline fun <reified T> String.fromJson(): T = GsonFactory.gson.fromJson(this, T::class.java)

inline fun <reified T> String.fromJsonArray(): List<T> = GsonFactory.fromJsonArray(this, T::class.java)

inline fun <reified T> Reader.fromJsonArray(): List<T> = GsonFactory.fromJsonArray(this, T::class.java)

object GsonFactory {
	val gson by lazy { Gson() }

	fun <T> fromJsonArray(string: String, clazz: Class<T>): List<T> = fromJsonArray(StringReader(string), clazz)

	fun <T> fromJsonArray(reader: Reader, clazz: Class<T>): List<T> {
		val type = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
		return gson.fromJson(reader, type)
	}

	class ParameterizedTypeImpl(private val raw: Class<*>, args: Array<Type>?) : ParameterizedType {
		private val args: Array<Type> = args ?: emptyArray()

		override fun getActualTypeArguments(): Array<Type> = args

		override fun getRawType(): Type = raw

		override fun getOwnerType(): Type? = null
	}
}