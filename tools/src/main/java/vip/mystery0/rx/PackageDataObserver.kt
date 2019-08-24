package vip.mystery0.rx

import androidx.lifecycle.Observer

interface PackageDataObserver<T> : Observer<PackageData<T>> {
	override fun onChanged(t: PackageData<T>) {
		when (t.status) {
			Status.Loading -> loading()
			Status.Content -> content(t.data)
			Status.Empty -> empty(t.data)
			Status.Error -> error(t.data, t.error)
		}
	}

	fun loading() {
	}

	fun empty(data: T?) {
	}

	fun content(data: T?) {
	}

	fun error(data: T?, e: Throwable?) {
	}
}