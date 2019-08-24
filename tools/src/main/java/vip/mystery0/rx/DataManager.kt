package vip.mystery0.rx

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors

fun <T> MutableLiveData<T>.doContent(data: T?) {
	this.postValue(data)
}

fun <T> MutableLiveData<PackageData<T>>.content(data: T?) {
	this.postValue(dataContent(data))
}

fun <T> MutableLiveData<PackageData<T>>.error(data: T?, error: Throwable?) {
	this.postValue(dataError(data, error))
}

fun <T> MutableLiveData<PackageData<T>>.error(error: Throwable?) {
	this.postValue(dataError(error))
}

fun <T> MutableLiveData<PackageData<T>>.empty(data: T?) {
	this.postValue(dataEmpty(data))

}

fun <T> MutableLiveData<PackageData<T>>.empty() {
	this.postValue(dataEmpty())
}

fun <T> MutableLiveData<PackageData<T>>.loading(data: T?) {
	this.postValue(dataLoading(data))
}

fun <T> MutableLiveData<PackageData<T>>.loading() {
	this.postValue(dataLoading())
}

class DataManager private constructor(threadNum: Int) {

	companion object {
		var instance: DataManager? = null
			private set

		@JvmStatic
		fun init(threadNum: Int) {
			if (instance != null) {
				instance!!.shutdown()
				instance = null
			}
			instance = DataManager(threadNum)
		}
	}

	private val threadPool by lazy { Executors.newFixedThreadPool(threadNum) }

	fun doRequest(action: () -> Unit) {
		threadPool.execute {
			action()
		}
	}

	fun <T> doRequest(liveData: MutableLiveData<PackageData<T>>, action: () -> Unit) {
		threadPool.execute {
			try {
				action()
			} catch (e: Exception) {
				liveData.error(e)
			}
		}
	}

	fun shutdown() {
		threadPool.shutdown()
	}
}