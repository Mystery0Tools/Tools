package vip.mystery0.tools.base

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder, in C : Any>(private val context: Context?,
																				@LayoutRes private val itemLayoutId: Int,
																				private val list: ArrayList<in C>) : RecyclerView.Adapter<T>() {
	override fun getItemCount(): Int = list.size

	override fun onBindViewHolder(holder: T, position: Int) {
		@Suppress("UNCHECKED_CAST")
		setItemView(holder, position, list[position] as C)
	}

	abstract fun setItemView(holder: T, position: Int, data: C)

	fun createView(parent: ViewGroup): View = LayoutInflater.from(context).inflate(itemLayoutId, parent, false)
}