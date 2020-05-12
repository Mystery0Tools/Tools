package vip.mystery0.rx

import androidx.lifecycle.Observer

interface PackageDataObserver<T> : Observer<PackageData<T>> {
	override fun onChanged(tPackageData: PackageData<T>) {
		when (tPackageData.status) {
			Status.Loading -> loading()
			Status.Content -> {
				content(tPackageData.data)
				tPackageData.data?.let { contentNoEmpty(it) }
			}
			Status.Empty -> {
				empty(tPackageData.data)
				empty()
			}
			Status.Error -> {
				error(tPackageData.data, tPackageData.error)
				tPackageData.data?.let { error(tPackageData.error) }
			}
		}
	}

	fun loading() {}

	fun empty(data: T?) {}

	fun empty() {}

	fun content(data: T?) {}

	fun contentNoEmpty(data: T) {}

	fun error(e: Throwable?) {}

	fun error(data: T?, e: Throwable?) {}
}
