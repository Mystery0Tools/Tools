package vip.mystery0.rx

import androidx.lifecycle.Observer

interface DataObserver<T> : Observer<PackageData<T>> {
	override fun onChanged(tPackageData: PackageData<T>) {
		when (tPackageData.status) {
			Status.Loading -> loading()
			Status.Content -> content(tPackageData.data)
			Status.Empty -> empty()
			Status.Error -> error(tPackageData.error)
		}
	}

	fun loading() {}
	fun empty() {}

	private fun content(data: T?) {
		if (data == null) {
			empty()
		} else {
			contentNoEmpty(data)
		}
	}

	fun contentNoEmpty(data: T) {}
	fun error(e: Throwable?) {}
}
