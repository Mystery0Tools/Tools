package vip.mystery0.rx

fun <T> content(data: T?): PackageData<T> = PackageData.content(data)
fun <T> error(data: T?, error: Throwable?): PackageData<T> = PackageData.error(data, error)
fun <T> error(error: Throwable?): PackageData<T> = PackageData.error(error)
fun <T> empty(data: T?): PackageData<T> = PackageData.empty(data)
fun <T> empty(): PackageData<T> = PackageData.empty()
fun <T> loading(data: T?): PackageData<T> = PackageData.loading(data)
fun <T> loading(): PackageData<T> = PackageData.loading()

class PackageData<T>(val status: Status,
					 val data: T?,
					 val error: Throwable?) {
	companion object {
		@JvmStatic
		fun <T> content(data: T?): PackageData<T> = PackageData(Status.Content, data, null)

		@JvmStatic
		fun <T> error(data: T?, error: Throwable?): PackageData<T> = PackageData(Status.Error, data, error)

		@JvmStatic
		fun <T> error(error: Throwable?): PackageData<T> = PackageData(Status.Error, null, error)

		@JvmStatic
		fun <T> empty(data: T?): PackageData<T> = PackageData(Status.Empty, data, null)

		@JvmStatic
		fun <T> empty(): PackageData<T> = PackageData(Status.Empty, null, null)

		@JvmStatic
		fun <T> loading(data: T?): PackageData<T> = PackageData(Status.Loading, data, null)

		@JvmStatic
		fun <T> loading(): PackageData<T> = PackageData(Status.Loading, null, null)
	}
}