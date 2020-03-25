package vip.mystery0.tools.model

/**
 * @author mystery0
 * Create at 2020/3/25
 */
class LRUCache<T>(private val maxSize: Int) : LinkedHashMap<Any, T>() {
	override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Any, T>?): Boolean = size > maxSize
}