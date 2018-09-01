package vip.mystery0.tools.base.binding

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import vip.mystery0.tools.base.BaseActivity

abstract class BaseBindingActivity<B : ViewDataBinding>(@LayoutRes layoutId: Int?) : BaseActivity(layoutId) {
	lateinit var binding: B

	override fun inflateView(layoutId: Int) {
		binding = DataBindingUtil.setContentView(this, layoutId)
	}
}