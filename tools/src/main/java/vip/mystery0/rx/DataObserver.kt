package vip.mystery0.rx

import androidx.lifecycle.Observer

interface DataObserver<T> : Observer<PackageData<T>> {
	override fun onChanged(tPackageData: PackageData<T>) {
		when (tPackageData.status) {
			Status.Loading -> loading()
			Status.Content -> content(tPackageData.data)
			Status.Empty -> empty(tPackageData.data)
			Status.Error -> error(tPackageData.data, tPackageData.error)
		}
	}

	fun loading() {}
	fun empty() {}
	fun empty(data: T?) {
		if (data == null) {
			empty()
		}
	}

	fun content(data: T?) {
		data?.let {
			contentNoEmpty(data)
		}
	}

	fun contentNoEmpty(data: T) {}
	fun error(e: Throwable?) {}
	fun error(data: T?, e: Throwable?) {
		if (data == null) {
			error(e)
		}
	}
}
