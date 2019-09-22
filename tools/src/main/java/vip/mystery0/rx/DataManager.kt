package vip.mystery0.rx

import androidx.lifecycle.MutableLiveData
import vip.mystery0.tools.doByTry
import java.util.concurrent.Executors

fun <T, D : MutableLiveData<T>> D.doContent(data: T?) = this.postValue(data)
fun <T, D : MutableLiveData<PackageData<T>>> D.content(data: T?) = this.postValue(dataContent(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.error(data: T?, error: Throwable?) = this.postValue(dataError(data, error))
fun <T, D : MutableLiveData<PackageData<T>>> D.error(error: Throwable?) = this.postValue(dataError(error))
fun <T, D : MutableLiveData<PackageData<T>>> D.empty(data: T?) = this.postValue(dataEmpty(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.empty() = this.postValue(dataEmpty())
fun <T, D : MutableLiveData<PackageData<T>>> D.loading(data: T?) = this.postValue(dataLoading(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.loading() = this.postValue(dataLoading())

fun <T, D : MutableLiveData<PackageData<T>>> D.doByCoroutine(`try`: (D) -> Unit) {
	val pair = doByTry(`try`)
	if (pair.second != null) {
		error(pair.second)
	}
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

		@JvmStatic
		fun instance(): DataManager = instance!!
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