package vip.mystery0.rx;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public interface PackageDataObserver<T> extends Observer<PackageData<T>> {
	@Override
	default void onChanged(PackageData<T> tPackageData) {
		switch (tPackageData.getStatus()) {
			case Loading:
				loading();
				break;
			case Content:
				content(tPackageData.getData());
				break;
			case Empty:
				empty(tPackageData.getData());
				break;
			case Error:
				error(tPackageData.getData(), tPackageData.getError());
				break;
			default:
				break;
		}
	}

	default void loading() {
	}

	default void empty(@Nullable T data) {
	}

	default void content(@Nullable T data) {
	}

	default void error(@Nullable T data, @Nullable Throwable e) {
	}
}
