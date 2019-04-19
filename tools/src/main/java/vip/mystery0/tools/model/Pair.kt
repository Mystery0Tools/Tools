package vip.mystery0.tools.model

import java.io.Serializable

data class Pair2<T1, T2>(
		var first: T1,
		var second: T2) : Serializable {
	override fun toString(): String = "(first=$first, second=$second)"
}

data class Pair3<T1, T2, T3>(
		var first: T1,
		var second: T2,
		var third: T3) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third)"
}

data class Pair4<T1, T2, T3, T4>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth)"
}

data class Pair5<T1, T2, T3, T4, T5>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth)"
}

data class Pair6<T1, T2, T3, T4, T5, T6>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5,
		var sixth: T6) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth, sixth=$sixth)"
}

data class Pair7<T1, T2, T3, T4, T5, T6, T7>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5,
		var sixth: T6,
		var seventh: T7) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth, sixth=$sixth, seventh=$seventh)"
}

data class Pair8<T1, T2, T3, T4, T5, T6, T7, T8>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5,
		var sixth: T6,
		var seventh: T7,
		var eighth: T8) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth, sixth=$sixth, seventh=$seventh, eighth=$eighth)"
}

data class Pair9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5,
		var sixth: T6,
		var seventh: T7,
		var eighth: T8,
		var ninth: T9) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth, sixth=$sixth, seventh=$seventh, eighth=$eighth, ninth=$ninth)"
}

data class Pair10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(
		var first: T1,
		var second: T2,
		var third: T3,
		var fourth: T4,
		var fifth: T5,
		var sixth: T6,
		var seventh: T7,
		var eighth: T8,
		var ninth: T9,
		var tenth: T10) : Serializable {
	override fun toString(): String = "(first=$first, second=$second, third=$third, fourth=$fourth, fifth=$fifth, sixth=$sixth, seventh=$seventh, eighth=$eighth, ninth=$ninth, tenth=$tenth)"
}