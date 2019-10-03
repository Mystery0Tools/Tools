package vip.mystery0.rx

import androidx.lifecycle.Observer

interface PackageDataObserver<T> : Observer<PackageData<T>> {
	override fun onChanged(tPackageData: PackageData<T>) {
		when (tPackageData.status) {
			Status.Loading -> loading()
			Status.Content -> content(tPackageData.data)
			Status.Empty -> empty(tPackageData.data)
			Status.Error -> error(tPackageData.data, tPackageData.error)
		}
	}

	fun loading() {}

	fun empty(data: T?) {}

	fun content(data: T?) {}

	fun error(data: T?, e: Throwable?) {}
}
