package vip.mystery0.tools.base.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingRecyclerViewAdapter<M : Any, B : ViewDataBinding>(@LayoutRes private val itemLayoutId: Int,
																			autoObserve: Boolean = true) : RecyclerView.Adapter<BaseBindingRecyclerViewAdapter.BaseBindingViewHolder>() {
	constructor(@LayoutRes itemLayoutId: Int) : this(itemLayoutId, true)

	var items = ObservableArrayList<M>()
	lateinit var itemsChangeCallback: ListChangedCallback

	init {
		if (autoObserve)
			itemsChangeCallback = ListChangedCallback()
	}

	override fun getItemCount(): Int = items.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
		val binding = DataBindingUtil.inflate<B>(LayoutInflater.from(parent.context), itemLayoutId, parent, false)
		return BaseBindingViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
		val binding = DataBindingUtil.getBinding<B>(holder.itemView)!!
		@Suppress("UNCHECKED_CAST")
		setItemView(binding, position, items[position] as M)
	}

	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		if (::itemsChangeCallback.isInitialized)
			this.items.addOnListChangedCallback(itemsChangeCallback)
	}

	override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
		super.onDetachedFromRecyclerView(recyclerView)
		if (::itemsChangeCallback.isInitialized)
			this.items.removeOnListChangedCallback(itemsChangeCallback)
	}

	//region 处理数据集变化
	fun onChanged(newItems: ObservableArrayList<M>) {
		resetItems(newItems)
		notifyDataSetChanged()
	}

	fun onItemRangeChanged(newItems: ObservableArrayList<M>, positionStart: Int, itemCount: Int) {
		resetItems(newItems)
		notifyItemRangeChanged(positionStart, itemCount)
	}

	fun onItemRangeInserted(newItems: ObservableArrayList<M>, positionStart: Int, itemCount: Int) {
		resetItems(newItems)
		notifyItemRangeInserted(positionStart, itemCount)
	}

	fun onItemRangeMoved(newItems: ObservableArrayList<M>) {
		resetItems(newItems)
		notifyDataSetChanged()
	}

	fun onItemRangeRemoved(newItems: ObservableArrayList<M>, positionStart: Int, itemCount: Int) {
		resetItems(newItems)
		notifyItemRangeRemoved(positionStart, itemCount)
	}

	private fun resetItems(newItems: ObservableArrayList<M>) {
		this.items = newItems
	}

	abstract fun setItemView(binding: B, position: Int, data: M)

	class BaseBindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

	inner class ListChangedCallback : ObservableList.OnListChangedCallback<ObservableArrayList<M>>() {
		override fun onChanged(newItems: ObservableArrayList<M>) {
			this@BaseBindingRecyclerViewAdapter.onChanged(newItems)
		}

		override fun onItemRangeChanged(newItems: ObservableArrayList<M>, i: Int, itemCount: Int) {
			this@BaseBindingRecyclerViewAdapter.onItemRangeChanged(newItems, i, itemCount)
		}

		override fun onItemRangeInserted(newItems: ObservableArrayList<M>, i: Int, itemCount: Int) {
			this@BaseBindingRecyclerViewAdapter.onItemRangeInserted(newItems, i, itemCount)
		}

		override fun onItemRangeMoved(newItems: ObservableArrayList<M>, i: Int, i1: Int, itemCount: Int) {
			this@BaseBindingRecyclerViewAdapter.onItemRangeMoved(newItems)
		}

		override fun onItemRangeRemoved(sender: ObservableArrayList<M>, positionStart: Int, itemCount: Int) {
			this@BaseBindingRecyclerViewAdapter.onItemRangeRemoved(sender, positionStart, itemCount)
		}
	}
}