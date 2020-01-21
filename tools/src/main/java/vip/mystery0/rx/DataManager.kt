package vip.mystery0.rx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

fun <T, D : MutableLiveData<T>> D.doContent(data: T?) = this.postValue(data)
fun <T, D : MutableLiveData<PackageData<T>>> D.content(data: T?) = this.postValue(dataContent(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.error(data: T?, error: Throwable?) = this.postValue(dataError(data, error))
fun <T, D : MutableLiveData<PackageData<T>>> D.error(error: Throwable?) = this.postValue(dataError(error))
fun <T, D : MutableLiveData<PackageData<T>>> D.empty(data: T?) = this.postValue(dataEmpty(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.empty() = this.postValue(dataEmpty())
fun <T, D : MutableLiveData<PackageData<T>>> D.loading(data: T?) = this.postValue(dataLoading(data))
fun <T, D : MutableLiveData<PackageData<T>>> D.loading() = this.postValue(dataLoading())

fun <T> ViewModel.launch(liveData: MutableLiveData<PackageData<T>>,
						 start: CoroutineStart = CoroutineStart.DEFAULT,
						 action: suspend CoroutineScope.() -> Unit) =
		viewModelScope.launch(dispatchException(liveData), start) { action() }

fun <T> dispatchException(liveData: MutableLiveData<PackageData<T>>): CoroutineExceptionHandler =
		CoroutineExceptionHandler { _, throwable -> liveData.error(throwable) }