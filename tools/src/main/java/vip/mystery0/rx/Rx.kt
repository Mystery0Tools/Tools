package vip.mystery0.rx

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class JustCompleteObserver<T> : OnlyCompleteObserver<T>() {
	override fun onError(e: Throwable) {
	}
}

abstract class OnlyCompleteObserver<T> : Observer<T> {
	private var data: T? = null

	override fun onNext(t: T) {
		data = t
	}

	override fun onComplete() {
		onFinish(data)
	}

	override fun onSubscribe(d: Disposable) {
	}

	abstract fun onFinish(data: T?)
}

abstract class StartAndCompleteObserver<T> : Observer<T> {
	private var data: T? = null

	override fun onNext(t: T) {
		data = t
	}

	override fun onComplete() {
		onFinish(data)
	}

	abstract fun onFinish(data: T?)
}

abstract class RxObserver<T> : Observer<T> {
	private var data: T? = null
	override fun onSubscribe(d: Disposable) {
		onStart()
	}

	override fun onNext(t: T) {
		data = t
	}

	override fun onComplete() {
		onFinish(data)
	}

	open fun onStart() {}

	abstract fun onFinish(data: T?)
}

class DoNothingObserver<T>(private val isLog: Boolean = false) : Observer<T> {
	companion object {
		private const val TAG = "DoNothingObserver"
	}

	override fun onComplete() {
		if (isLog)
			Log.d(TAG, "onComplete: ")
	}

	override fun onSubscribe(d: Disposable) {
		if (isLog)
			Log.d(TAG, "onSubscribe: ")
	}

	override fun onNext(t: T) {
		if (isLog)
			Log.d(TAG, "onNext: ")
	}

	override fun onError(e: Throwable) {
		if (isLog)
			Log.e(TAG, "onError: ", e)
	}
}