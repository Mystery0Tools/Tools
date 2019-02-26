package vip.mystery0.tools.base.binding

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class BaseMultiBindingRecyclerViewAdapter : BaseBindingRecyclerViewAdapter<Any, ViewDataBinding>(0) {
	abstract fun bindViewType(position: Int, data: Any): Int

	abstract fun createBinding(viewType: Int): ViewDataBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder = BaseBindingViewHolder(createBinding(viewType).root)

	override fun getItemViewType(position: Int): Int = bindViewType(position, items[position])
}