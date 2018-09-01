package vip.mystery0.tools.base.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import vip.mystery0.tools.base.BaseFragment

abstract class BaseBindingFragment<B : ViewDataBinding>(@LayoutRes layoutId: Int) : BaseFragment(layoutId) {
	lateinit var binding: B

	override fun inflateView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?): View {
		binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
		return binding.root
	}
}